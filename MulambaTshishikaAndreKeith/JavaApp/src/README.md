Application de Gestion des Boutiques et Articles - README
1. Introduction
   Cette application permet de gérer des boutiques, des articles associés, des employés, et des utilisateurs. Elle inclut des fonctionnalités pour afficher les boutiques, gérer les articles, ainsi que gérer les utilisateurs et leurs authentifications.

L'application est construite en Java avec Swing pour l'interface graphique et MySQL pour la gestion de la base de données.

2. Prérequis
   Avant d'exécuter l'application, vous devez disposer des éléments suivants :

Java 8+ installé sur votre machine.
MySQL avec une base de données configurée pour le projet.
JDBC pour connecter l'application à la base de données MySQL.
3. Structure du projet
   Le projet est organisé de la manière suivante :

app/ : Contient toutes les classes Java principales pour l'application.

Boutique.java : Représente une boutique.
BoutiqueFrame.java : Affiche les boutiques sous forme de boutons hexagonaux.
BoutiqueManager.java : Permet de gérer les boutiques (ajouter, modifier, supprimer).
ArticleFrame.java : Affiche les articles d'une boutique spécifique.
ArticleManager.java : Permet de gérer les articles d'une boutique (ajouter, modifier, supprimer).
CRUDFrame.java : Gestion des opérations CRUD pour différents objets dans l'application.
EmployeManager.java : Gestion des employés de la boutique.
ListBlancheFrame.java : Gère la liste blanche des utilisateurs autorisés.
UserListFrame.java : Affiche la liste des utilisateurs et leurs informations.
UserMainPage.java : Page principale pour les utilisateurs après authentification.
auth/ : Contient les classes pour l'authentification des utilisateurs.

SignUp.java : Formulaire pour inscrire un nouvel utilisateur.
LogIn.java : Formulaire pour connecter un utilisateur existant.
DbHelper.java : Gère la connexion à la base de données.

4. Classes supplémentaires
   4.1. Boutique.java
   La classe Boutique représente un objet boutique avec un id et un nom. Elle offre des méthodes pour accéder à ces informations :

getId() : Retourne l'identifiant de la boutique.
getNom() : Retourne le nom de la boutique.
4.2. BoutiqueFrame.java
BoutiqueFrame est une fenêtre qui affiche une liste de boutiques sous forme de boutons hexagonaux. Lorsqu'un utilisateur clique sur un bouton, l'application affiche les articles associés à cette boutique dans une nouvelle fenêtre ArticleFrame.

La fenêtre est maximisée et un titre défilant indique le nom de l'application. Un bouton "Retour" permet de revenir à la page principale.

4.3. BoutiqueManager.java
BoutiqueManager est une fenêtre permettant de gérer les boutiques : ajouter, mettre à jour ou supprimer des boutiques. Elle affiche un tableau des boutiques et inclut des boutons pour effectuer des actions.

4.4. ArticleFrame.java
ArticleFrame permet d'afficher les articles associés à une boutique spécifique. Cette fenêtre est accessible en cliquant sur une boutique dans BoutiqueFrame.

4.5. ArticleManager.java
ArticleManager gère les opérations sur les articles d'une boutique : ajout, modification et suppression. Elle affiche un tableau des articles et inclut des boutons d'action pour chaque opération.

4.6. CRUDFrame.java
CRUDFrame est une fenêtre générique qui permet de gérer les opérations de type CRUD (Créer, Lire, Mettre à jour, Supprimer) pour différents objets dans l'application. Elle peut être utilisée pour gérer des boutiques, des articles ou d'autres entités.

4.7. EmployeManager.java
EmployeManager permet de gérer les employés associés à une boutique. Cette fenêtre affiche une liste d'employés et inclut des fonctionnalités pour ajouter, modifier ou supprimer un employé.

4.8. ListBlancheFrame.java
ListBlancheFrame permet de gérer la liste blanche des utilisateurs autorisés à accéder à certaines fonctionnalités ou sections de l'application. Elle affiche une liste des utilisateurs autorisés et permet d'ajouter ou de supprimer des utilisateurs.

4.9. UserListFrame.java
UserListFrame permet d'afficher la liste des utilisateurs enregistrés dans l'application. Elle inclut des fonctionnalités pour voir les détails des utilisateurs et effectuer des actions administratives.

4.10. UserMainPage.java
UserMainPage est la page principale à laquelle les utilisateurs accèdent après leur authentification. Elle permet de naviguer dans les différentes sections de l'application selon les rôles de l'utilisateur.

4.11. SignUp.java
SignUp est un formulaire d'inscription pour les utilisateurs qui souhaitent créer un compte dans l'application. Elle permet de saisir des informations telles que le nom, l'e-mail et le mot de passe, et de les enregistrer dans la base de données.

4.12. LogIn.java
LogIn est un formulaire de connexion pour les utilisateurs qui souhaitent se connecter à l'application. Elle vérifie les informations d'identification et redirige les utilisateurs vers la page principale après une connexion réussie.

5. Base de données
   Structure de la table boutique
   La table boutique dans la base de données MySQL contient les colonnes suivantes :

id (INT, clé primaire) : Identifiant unique de la boutique.
nom (VARCHAR) : Nom de la boutique.
date_creation (DATETIME) : Date de création de la boutique.
Structure des autres tables
En plus de la table boutique, l'application utilise des tables pour gérer les articles, les employés, et les utilisateurs. Assurez-vous que toutes les tables nécessaires sont créées dans la base de données avant d'exécuter l'application.

6. Fonctionnalités détaillées
   Gestion des Boutiques et des Articles
   Les utilisateurs peuvent :

Créer une boutique : En saisissant un nom pour la nouvelle boutique.
Mettre à jour une boutique : En modifiant le nom d'une boutique existante.
Supprimer une boutique : En supprimant une boutique de la base de données.
Les articles associés à chaque boutique peuvent également être gérés :

Créer un article : En saisissant les informations de l'article (nom, prix, description, etc.).
Mettre à jour un article : En modifiant les informations d'un article existant.
Supprimer un article : En supprimant un article de la base de données.
Authentification des Utilisateurs
L'application permet aux utilisateurs de s'inscrire et de se connecter via les formulaires SignUp et LogIn. Une fois connectés, ils peuvent accéder à la page principale et naviguer dans les différentes sections de l'application.

