# task-api

Application de gestion de tâches développée avec Java et Spring Boot, exposant une API REST et une interface web simple permettant de manipuler les tâches depuis un navigateur.

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Architecture](#architecture)
- [Modèle de données](#modèle-de-données)
- [API REST](#api-rest)
- [Interface web](#interface-web)
- [Validation et gestion des erreurs](#validation-et-gestion-des-erreurs)
- [Tests](#tests)
- [Base de données](#base-de-données)
- [Docker](#docker)
- [Cloud Run](#cloud-run)
- [Lancer le projet en local](#lancer-le-projet-en-local)

## Fonctionnalités

L'application permet de :

- créer une tâche ;
- récupérer toutes les tâches ;
- récupérer une tâche par son identifiant ;
- modifier une tâche ;
- supprimer une tâche ;
- gérer trois statuts : `TODO`, `IN_PROGRESS` et `DONE` ;
- attribuer des Story Points à une tâche ;
- manipuler les tâches directement depuis une interface web.

## Architecture

```text
Interface web / Client HTTP
        ↓
    Controller
        ↓
     Service
        ↓
   Repository
        ↓
   PostgreSQL
```

Le projet suit une architecture en couches :

- **Controller** : reçoit les requêtes HTTP et renvoie les réponses ;
- **Service** : contient la logique métier ;
- **Repository** : utilise Spring Data JPA pour accéder aux données ;
- **Entity** : représente les tâches stockées en base.

Le repository repose sur `JpaRepository`, qui fournit notamment les opérations `findAll()`, `findById()`, `save()` et `delete()`.

## Modèle de données

Une tâche contient notamment :

```json
{
  "id": 1,
  "title": "Configurer Docker",
  "description": "Créer le Dockerfile",
  "status": "TODO",
  "storyPoints": 5,
  "createdAt": "2026-09-09T20:00:00"
}
```

Les statuts possibles sont :

- `TODO`
- `IN_PROGRESS`
- `DONE`

Les Story Points proposés dans l'interface sont :

```text
1, 2, 3, 5, 8, 13, 21
```

## API REST

| Méthode | Route | Description |
|---|---|---|
| GET | `/health` | Vérifie que l'application répond |
| GET | `/tasks` | Récupère toutes les tâches |
| GET | `/tasks/{id}` | Récupère une tâche précise |
| POST | `/tasks` | Crée une tâche |
| PUT | `/tasks/{id}` | Modifie une tâche |
| DELETE | `/tasks/{id}` | Supprime une tâche |

## Interface web

Une interface web en HTML, CSS et JavaScript permet d'utiliser l'API directement depuis le navigateur.

Elle permet de :

- créer une tâche avec un titre, une description et des Story Points ;
- afficher les tâches selon leur statut ;
- modifier une tâche existante ;
- changer son statut ;
- modifier ses Story Points ;
- supprimer une tâche.

Les tâches sont réparties dans trois catégories :

```text
À faire
En cours
Terminé
```

L'interface communique directement avec l'API `/tasks` grâce à `fetch()` et utilise les méthodes HTTP `GET`, `POST`, `PUT` et `DELETE`.

## Validation et gestion des erreurs

L'API valide les données reçues et renvoie des statuts HTTP cohérents.

Principaux cas :

- `200 OK` : requête réussie ;
- `201 Created` : tâche créée ;
- `400 Bad Request` : données invalides ;
- `404 Not Found` : tâche inexistante.

La gestion des erreurs permet notamment de renvoyer `404` lorsqu'un identifiant de tâche n'existe pas.

Les Story Points sont également validés : une valeur autorisée comme `5` est acceptée, tandis qu'une valeur comme `4` provoque une réponse `400 Bad Request`.

## Tests

Les tests sont écrits avec **JUnit** et **MockMvc**.

Ils couvrent notamment :

- `GET /tasks` ;
- récupération d'une tâche existante ;
- récupération d'un identifiant inexistant ;
- création d'une tâche ;
- validation d'un titre vide ;
- validation du statut ;
- validation des Story Points ;
- modification d'une tâche ;
- modification d'un identifiant inexistant ;
- suppression d'une tâche ;
- suppression d'un identifiant inexistant.

Les tests vérifient aussi bien les réponses HTTP que le contenu JSON retourné par l'API.

Pour lancer les tests :

```bash
./mvnw test
```

Sous Windows :

```powershell
.\mvnw.cmd test
```

## Base de données

L'application utilise **PostgreSQL** avec Spring Data JPA et Hibernate.

En local, la base utilisée est :

```text
jdbc:postgresql://localhost:5433/taskdb
```

En production, PostgreSQL est hébergé sur **Google Cloud SQL**.

La connexion entre Cloud Run et Cloud SQL utilise le connecteur Java Cloud SQL (`postgres-socket-factory`) et des variables d'environnement pour éviter de coder les informations de connexion directement dans l'application.

## Docker

L'application est conteneurisée avec Docker et utilise Java 21.

Construire l'image :

```bash
docker build -t task-api .
```

Lancer le conteneur en local sous Windows :

```powershell
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/taskdb -e SPRING_DATASOURCE_USERNAME=postgres -e SPRING_DATASOURCE_PASSWORD=YOUR_PASSWORD task-api
```

`host.docker.internal` permet au conteneur Docker d'accéder à PostgreSQL exécuté sur la machine hôte.

## Cloud Run

L'application est déployée sur **Google Cloud Run** en région `europe-west9`.

L'image Docker est stockée dans **Artifact Registry** puis utilisée par Cloud Run.

La base PostgreSQL de production est hébergée sur **Cloud SQL**. Le service Cloud Run dispose du rôle IAM **Cloud SQL Client** afin d'accéder à l'instance PostgreSQL.

Les paramètres de connexion sont fournis via des variables d'environnement :

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

URL publique :

```text
https://task-api-939034790424.europe-west9.run.app/
```

Endpoint de vérification :

```text
https://task-api-939034790424.europe-west9.run.app/health
```

## Lancer le projet en local

Prérequis :

- Java 21 ;
- PostgreSQL ;
- Maven Wrapper inclus dans le projet.

Lancer PostgreSQL puis démarrer l'application :

```powershell
.\mvnw.cmd spring-boot:run
```

L'application est ensuite disponible sur :

```text
http://localhost:8080
```

L'interface web permet alors d'utiliser l'application directement depuis le navigateur.

La documentation Swagger est disponible sur :

```text
http://localhost:8080/swagger-ui/index.html
```

## Limites du projet

- **Interface volontairement minimaliste** : l’interface web est réalisée en HTML/CSS/JavaScript sans framework frontend. Elle permet de manipuler les tâches et de démontrer le fonctionnement de l’API, mais n’a pas vocation à remplacer une application frontend complète.

- **Pas d’authentification** : l’application est publique et ne gère ni comptes utilisateurs, ni rôles, ni permissions. Dans une application réelle, une couche d’authentification et d’autorisation serait nécessaire avant d’exposer des données utilisateur.

- **Gestion des secrets** : les paramètres de connexion à PostgreSQL sont injectés dans Cloud Run via des variables d’environnement et ne sont pas codés en dur dans le projet. Pour une application de production, un gestionnaire de secrets comme **Google Secret Manager** serait préférable pour stocker notamment le mot de passe de la base.

- **Périmètre métier volontairement réduit** : le projet se concentre sur une seule ressource `Task` et sur les opérations CRUD associées. L’objectif est de démontrer une architecture Spring Boot complète — API REST, validation, persistance PostgreSQL, tests, Docker et déploiement Cloud — plutôt que de construire une application de gestion de projet complète.