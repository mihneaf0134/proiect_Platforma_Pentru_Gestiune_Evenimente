import sys

sys.path.insert(1, 'generated')

from generated import auth_pb2, auth_pb2_grpc


import grpc
from concurrent import futures
import jwt


from db import SessionLocal, User
from jwt_utils import generate_token, decode_token
import blacklist

class AuthService(auth_pb2_grpc.AuthServiceServicer):

    def Login(self, request, context):
        db = SessionLocal()
        user = db.query(User).filter_by(
            email=request.email,
            password=request.password
        ).first()

        if not user:
            context.abort(grpc.StatusCode.UNAUTHENTICATED, "Invalid credentials")

        token = generate_token(user)
        return auth_pb2.LoginResponse(token_value=token)

    def ValidateToken(self, request, context):
        if blacklist.contains(request.token):
            return auth_pb2.ValidateResponse(success=False)

        try:
            payload = decode_token(request.token)
            return auth_pb2.ValidateResponse(
                success=True,
                sub=int(payload["sub"]),
                role=payload["role"],
                email = payload["email"]
            )
        except jwt.ExpiredSignatureError:
            return auth_pb2.ValidateResponse(success=False)

    def Logout(self, request, context):
        blacklist.add(request.token)
        return auth_pb2.Empty()

    def CreateUser(self, request, context):
        try:
            payload = decode_token(request.admin_token)
        except Exception:
            context.abort(grpc.StatusCode.UNAUTHENTICATED, "Invalid admin token")

        if payload["role"] != "admin":
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "Admin role required")

        if request.role not in ["admin", "owner-event", "client"]:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Invalid role")

        db = SessionLocal()

        if db.query(User).filter_by(email=request.email).first():
            context.abort(grpc.StatusCode.ALREADY_EXISTS, "User already exists")

        user = User(
            email=request.email,
            password=request.password,
            role=request.role
        )
        db.add(user)
        db.commit()
        db.refresh(user)

        return auth_pb2.CreateUserResponse(
            success=True,
            user_id=user.id
        )

    def DeleteUser(self, request, context):

        db = SessionLocal()
        user = db.query(User).filter_by(id=request.user_id).first()

        if not user:
            context.abort(grpc.StatusCode.NOT_FOUND, "User not found")

        db.delete(user)
        db.commit()

        return auth_pb2.DeleteUserResponse(success=True)

    def GetAllUsers(self, request, context):

        db = SessionLocal()
        users = db.query(User).all()

        return auth_pb2.GetAllUsersResponse(
            users=[
                auth_pb2.UserDTO(
                    id=u.id,
                    email=u.email,
                    role=u.role
                ) for u in users
            ]
        )

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    auth_pb2_grpc.add_AuthServiceServicer_to_server(AuthService(), server)
    server.add_insecure_port("[::]:50051")
    server.start()
    print("gRPC Auth Service running on port 50051")
    server.wait_for_termination()

if __name__ == "__main__":
    serve()
