def newboard(n):
    p_pionsnoir = 1
    p_pionsblanc = 2
    board = [[p_pionsnoir] * n for i in range(n)]
    for i in range (n):
        for t in range(n):
            board[i][t] = p_pionsnoir
            p_pionsnoir, p_pionsblanc = p_pionsblanc, p_pionsnoir
    milieu = n // 2
    board[milieu][milieu] = 0

    return board

def display(board, n):
    for _ in range(n):
        print( _ , '|', end = "\t")
        for t in range(n):
            if board[_][t] == 2:
                print("x",  end = " ")
            if board[_][t] == 1:
                print("o", end = " ")
            if board[_][t] == 0:
                print(".", end = " ")
        print("")
    print("\t", end = "")   
    for i in range(n):
        print('_', end = " ")
    print('')
    print("\t", end = "")
    for i in range(n):
        print(i, end= " ")
    print('')

def possiblePawn(board, n, player, i, j, casevide):
    print(n, player, i, j, casevide)
    chemin = [(-1, 0), (1, 0), (0, -1), (0, 1)]
    casevide = [i,j]
    for ci, cj in chemin:
        ni, nj = i + ci, j + cj
        if 0 <= ni < n and 0 <= nj < n and board[ni][nj] == 0:
            return True, [ni, nj]
    return False, casevide
        
def selectPawn(board, n, player):
    print("Le joueur",player,"selectionnez la ligne du pions que vous souhaitiez déplacés: ")
    s_lignepions = eval(input())
    while not ( 0 <= s_lignepions ):
        print("Impossible. Joueur",player, "vous devez ressayez: ")
        s_lignepions = eval(input())
    print("Le joueur",player,"selectionnez la colonnes du pions que vous souhaitiez déplacés: ")
    s_colonnepions = eval(input())
    while not ( 0 <= s_colonnepions < n):
        print("Impossible. Joueur",player, "Vous devez ressayez: ")
        s_colonnepions = eval(input())

    return s_lignepions, s_colonnepions

def updateBoard(board, n, i, j, casevide):
    board[i][j], board[casevide[0]][casevide[1]], casevide[0], casevide[1] = board[casevide[0]][casevide[1]],
    board[i][j]
    
    return board, casevide

def again(board, n, player, i, j, casevide):
    chemin = [(-1, 0), (1, 0), (0,-1), (0, 1)]
    return any(
        0 <= casevide[0] + ci < n and 0 <= casevide[1] + cj < n and
        board[casevide[0] + ci][casevide[1] + cj] == player
        for ci, cj in chemin
    )
    
def inputBoard():
    n = eval(input("Donnez la taille du tableau de jeu: "))
    for i in range (100):
        if n == (i * 4) + 1:
            return n
    return 9
    
def lewthwaite(n):
    board = newboard(n)
    casevide = [0,0]
    player = 1
    Newtour = True
    while Newtour:
        player = 3 - player
        possiblepion = False
        display(board, n)
        while not possiblepion:
            i, j = selectPawn(board, n, player)
            possiblepion, casevide = possiblePawn(board, n, player, i, j, casevide)
        board, casevide = updateBoard(board, n, i, j, casevide)
        Newtour = again(board, n, i, j, casevide)
        display(board, n)
        print("Félicitation, le joueur",3 - player,"a gagné !")

n = inputBoard()
lewthwaite(n)