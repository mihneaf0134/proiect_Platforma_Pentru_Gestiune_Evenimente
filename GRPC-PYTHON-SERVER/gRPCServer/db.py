from sqlalchemy import *
from sqlalchemy.orm import *


Base = declarative_base()

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True)
    email = Column(String(100), unique=True)
    password = Column(String(255))
    role = Column(Enum("admin","owner-event","client"))

engine = create_engine(
    "mysql+pymysql://auth:auth@mysql-auth:3306/authdb",
    pool_pre_ping=True
)

SessionLocal = sessionmaker(bind=engine)


