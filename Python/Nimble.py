import random

def newboard(n,p):
    board = []
    for _ in range(n):
        board.append(random.randint(0,p))
        
    return board


def display(board, n):
    for i in range(n):
        pions = board[i]
        print(f"{pions:2d} | ", end="")

    print()
    ligne = "-" * (5 * n)
    print(ligne)

    for i in range(1, n + 1):
        print(f"{i:2d} | ", end="")

    print()


def possibleSquare(board, n, i):
    p_indice = 0 <= 1 < n
    p_pions = board[i] > 0

    return ( p_indice and p_pions )

def selectSquare(board, n):
    while True:
        i = int(input("Quel case: ")) - 1
        if possibleSquare(board, n, i):
            return i
        else:
            print("Impossible. Essayez une autre. ")

def possibleDestination(board, n, i, j):
    p_pions = 0 <= 1 < n and board[i] > 0
    p_destination = 0 <= j < i

    return ( p_pions and p_destination )

def selectDestination(board, n, i ):
    while True:
        j = int(input("Vers? :")) - 1
        if possibleDestination(board, n, i, j):
            return j
        else:
            print("Impossible. Réessayez")

def move(board, i, j):
    board[j] = board[j] + 1
    board[i] = board[i] - 1 

def lose(board, n):
    for _ in range(1, n):
        if board[_] > 0:
            return False
    return True 

def nimble(n, p):
    board = newboard(n,p)
    display(board, n)
    joueur = 1
    while not lose(board, n):
        print("Joueur" + str(joueur))
        i = selectSquare(board, n)
        j = selectDestination(board, n, i)
        move(board, i, j)
        joueur = 3 - joueur
        display(board, n)
    print("Le gagnant est " + str(3 - joueur))

nimble(6, 4)

    


    




