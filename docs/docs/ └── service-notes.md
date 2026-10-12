# Atelier 4 — IoC, Injection de dépendances et couche Service

## A. Injection de dépendances

### Injection par champ

L'injection par champ utilise `@Autowired` directement sur un attribut. Elle permet à Spring d'injecter automatiquement une dépendance, mais rend les dépendances moins explicites et complique les tests unitaires.

### Injection par constructeur

L'injection par constructeur permet de déclarer explicitement les dépendances nécessaires au fonctionnement d'un service.

Dans notre projet AutoLoc, nous utilisons `@RequiredArgsConstructor` de Lombok et des attributs `final`.

**Choix retenu :** injection par constructeur, car elle favorise l'immutabilité des références, facilite les tests et évite les dépendances cachées.

## B. Services et dépendances

| Service | Repository injecté | Opérations |
|---|---|---|
| AgenceServiceImpl | IAgenceRepository | CRUD |
| ClientServiceImpl | IClientRepository | CRUD |
| ContratServiceImpl | IContratRepository | CRUD |
| EmployeServiceImpl | IEmployeRepository | CRUD |
| EquipementServiceImpl | IEquipementRepository | CRUD |
| MaintenanceServiceImpl | IMaintenanceRepository | CRUD |
| PaiementServiceImpl | IPaiementRepository | Lecture seule |
| ReservationServiceImpl | IReservationRepository | CRUD |
| VehiculeServiceImpl | IVehiculeRepository | CRUD |

Les services sont annotés avec `@Service` et utilisent l'injection par constructeur.

La couche Service centralise les validations métier et sépare la logique applicative de la couche d'accès aux données.

## C. Diagnostic des erreurs Spring

### Erreur 1 — Missing Bean

**Cause :** `ContratServiceImpl` dépendait d'une interface `MissingService` sans implémentation enregistrée dans Spring.

**Résultat attendu :** échec du démarrage avec un message indiquant qu'aucun bean correspondant n'a été trouvé.

**Correction :** suppression de la dépendance inutile.

### Erreur 2 — Multiple Beans

**Cause :** deux classes, `EmailNotificationService` et `SmsNotificationService`, implémentaient `INotificationService`.

Spring ne pouvait pas choisir automatiquement entre les deux.

**Correction :** utilisation de `@Qualifier("emailNotificationService")` sur le paramètre du constructeur.

### Erreur 3 — Circular Dependency

**Cause :** `ServiceA` dépendait de `ServiceB`, tandis que `ServiceB` dépendait de `ServiceA`.

**Correction :** suppression de la dépendance de `ServiceB` vers `ServiceA`, afin d'obtenir une dépendance à sens unique.

Les versions contenant les erreurs et leurs corrections sont conservées dans l'historique Git du projet.

## D. Analyse SonarQube for IDE

### Règle S6813 — Field Injection

L'injection par champ est déconseillée. Elle a été remplacée par l'injection par constructeur.

### Règle S1068 — Unused Private Field

SonarQube for IDE a signalé un attribut `notificationService` déclaré mais non utilisé.

Cet attribut a été supprimé lors du nettoyage des classes temporaires.

### Règle S112 — Generic Exceptions

Une version temporaire de `ContratServiceImpl` utilisait `RuntimeException` pour signaler l'absence d'un contrat.

La correction consiste à utiliser `ResourceNotFoundException`, une exception métier spécifique.

Le code problématique et sa correction ont été conservés dans des commits distincts. La règle S112 n'a pas été observée dans les résultats affichés par le plugin pendant l'exercice.

## E. Questions de compréhension

### 1. Qu'est-ce que l'inversion de contrôle ?

L'inversion de contrôle (IoC) signifie que la création et la gestion des objets sont confiées au conteneur Spring plutôt qu'effectuées directement par les classes applicatives.

### 2. Quelle est la différence entre IoC et injection de dépendances ?

L'IoC est un principe architectural. L'injection de dépendances est un mécanisme permettant de le mettre en œuvre.

### 3. Pourquoi utiliser `@Service` ?

`@Service` permet à Spring de détecter une classe comme composant de la couche métier et de la gérer comme un bean.

### 4. Pourquoi préférer l'injection par constructeur ?

Elle rend les dépendances explicites, permet d'utiliser des champs `final` et simplifie les tests unitaires.

### 5. Pourquoi séparer Repository et Service ?

Le Repository gère l'accès aux données et les opérations de persistance. Le Service contient les règles métier, les validations et l'orchestration des opérations.

Cette séparation améliore la lisibilité, la maintenance et la testabilité du projet.