import jwt
import datetime
import uuid

SECRET = "jwt-secret"
ALGO = "HS256"


def generate_token(user):
    payload = {
        "iss": "http://localhost:50051",
        "sub": str(user.id),
        "email": user.email,
        "role": user.role,
        "exp": datetime.datetime.utcnow() + datetime.timedelta(hours=3),
        "jti": str(uuid.uuid4())
    }

    return jwt.encode(payload, SECRET, algorithm=ALGO)

def decode_token(token):
    return jwt.decode(token, SECRET, algorithms=[ALGO])
