blacklist = set()

def add(token):
    blacklist.add(token)

def contains(token):
    return token in blacklist

