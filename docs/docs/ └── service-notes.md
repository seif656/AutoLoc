# Atelier 4 — IoC, injection de dépendances et couche Service

**Projet : AutoLoc**  
**Technologies :** Java 17+, Spring Boot, Spring Data JPA, Lombok, Maven, SonarQube for IDE

## A. Comparaison des modes d'injection

| Critère | Injection par constructeur | Injection par attribut |
|---|---|---|
| Principe | Dépendance fournie au constructeur | Dépendance injectée directement dans un champ avec `@Autowired` |
| Champ `final` | Oui | Non, dans le cas de l'injection habituelle |
| Dépendance explicite | Oui, dans la signature du constructeur | Non, elle est cachée dans les attributs |
| Utilisation hors du conteneur Spring | Oui, en fournissant les dépendances au constructeur | Risque de dépendance `null` lors d'une instanciation manuelle |
| Tests unitaires | Faciles, avec des repositories simulés | Plus difficiles, car les dépendances sont injectées dans les champs |
| SonarQube for IDE | Mode recommandé | Règle S6813 à vérifier selon l'analyse |
| Choix pour AutoLoc | Retenu | Testé temporairement, puis abandonné |

Nous avons retenu l'injection par constructeur pour les services AutoLoc.

Les classes utilisent `@Service`, `@RequiredArgsConstructor` et un attribut `private final` pour leur repository.

Lombok génère le constructeur nécessaire. Spring l'utilise automatiquement pour injecter le repository, sans avoir besoin de `@Autowired` sur un constructeur unique.

L'injection par setter constitue un troisième mode, adapté principalement aux dépendances optionnelles ou modifiables. Elle n'est pas retenue pour les dépendances obligatoires de nos services.

## B. Services et dépendances

| Service | Dépendance injectée | Mode | Justification |
|---|---|---|---|
| AgenceServiceImpl | IAgenceRepository | Constructeur | Gérer les agences et valider les informations obligatoires |
| ClientServiceImpl | IClientRepository | Constructeur | Gérer les clients et contrôler leurs informations d'identification |
| ContratServiceImpl | IContratRepository | Constructeur | Gérer les contrats et valider les montants |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Gérer les employés et leurs informations obligatoires |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Gérer les équipements et vérifier leur libellé |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Gérer les maintenances et vérifier la cohérence des dates |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Consulter les paiements sans exposer de modification directe |
| ReservationServiceImpl | IReservationRepository | Constructeur | Gérer les réservations et valider leurs dates |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Gérer les véhicules et vérifier les données obligatoires et le tarif |

Les services CRUD exposent cinq méthodes : `create`, `findById`, `findAll`, `update` et `deleteById`.

Le service Paiement est volontairement limité à `findById` et `findAll`, conformément à l'énoncé.

Les principales règles appliquées sont les suivantes :

- Une création refuse un identifiant déjà renseigné.
- Une recherche ou une suppression d'un élément inexistant déclenche `ResourceNotFoundException`.
- Une mise à jour charge l'entité existante, recopie ses champs modifiables, puis l'enregistre.
- Les valeurs métier invalides provoquent une `IllegalArgumentException`.
- Les services ne conservent aucun état propre à une requête.
- Les relations entre entités sont conservées sans ajouter de gestion métier des associations, prévue dans un chapitre ultérieur.

Les validations ont été adaptées aux attributs réellement présents dans les entités du projet.

## C. Diagnostic des trois messages d'erreur Spring

### Message 1 — Bean IContratService introuvable

**Exception :** `NoSuchBeanDefinitionException`, généralement remontée dans une `UnsatisfiedDependencyException`.

**Bean concerné :** `IContratService`, demandé par `ContratController`.

**Deux causes probables :**

1. `ContratServiceImpl` ne possède pas l'annotation `@Service`.
2. `ContratServiceImpl` est placé dans un package qui n'est pas couvert par le component scan de Spring.

**Correction :** ajouter ou restaurer `@Service` sur `ContratServiceImpl`, puis vérifier que la classe se trouve dans un package analysé par Spring Boot.

### Message 2 — Plusieurs beans pour une même interface

**Exception :** `NoUniqueBeanDefinitionException`.

**Bean concerné :** la dépendance du contrôleur de notification, pour laquelle deux implémentations existent : `emailNotificateur` et `smsNotificateur`.

**Cause :** Spring trouve plusieurs beans compatibles et ne sait pas lequel injecter.

**Deux corrections possibles :**

1. Ajouter `@Primary` sur la classe d'implémentation à privilégier. Cette annotation définit le bean prioritaire.
2. Ajouter `@Qualifier("emailNotificateur")` sur le paramètre du constructeur du consommateur pour sélectionner explicitement le bean voulu.

`@Primary` définit un choix par défaut, tandis que `@Qualifier` sélectionne une implémentation à un point d'injection précis.

### Message 3 — Dépendance circulaire

**Exception :** `BeanCurrentlyInCreationException`, éventuellement incluse dans une erreur de création de bean.

**Beans concernés :** `clientServiceImpl` et `reservationServiceImpl`.

**Cause :** le premier service dépend du second, qui dépend à son tour du premier.

**Pourquoi l'injection par constructeur bloque-t-elle ?**

Chaque objet doit recevoir toutes ses dépendances au moment de sa construction. Spring ne peut donc pas terminer la création du premier service sans créer le second, qui exige lui-même le premier.

**Correction :** revoir la conception pour supprimer la dépendance réciproque, par exemple en extrayant une responsabilité commune dans un troisième service ou en organisant les appels dans un seul sens.

## D. Analyse SonarQube for IDE

| Règle | Anomalie | Explication | Correction |
|---|---|---|---|
| S6813 | Injection par attribut | L'utilisation de `@Autowired` sur un champ masque les dépendances et complique les tests | Retour à l'injection par constructeur avec `@RequiredArgsConstructor` et champ `final` |
| S112 | Exception générique | Une `RuntimeException` générique ne décrit pas précisément l'erreur métier | Utilisation de `ResourceNotFoundException` |
| S1068 | Attribut privé inutilisé | Un attribut `notificationService` était déclaré sans être utilisé | Suppression de l'attribut inutile |

**Précision sur les observations :**

La règle S1068 a été observée dans SonarQube for IDE.

Les cas S6813 et S112 ont été étudiés dans les expériences prévues par l'atelier. Le déclenchement effectif de ces deux règles n'a pas été confirmé dans les résultats observés. Les corrections correspondantes ont néanmoins été appliquées.

Les classes temporaires utilisées pour les expériences d'injection de dépendances ont été retirées du code final.

## E. Questions de compréhension

### 1. Où est le `new` de ContratServiceImpl et qui l'exécute ?

Nous n'écrivons pas directement `new ContratServiceImpl(...)` dans le code applicatif.

Le conteneur IoC de Spring détecte `ContratServiceImpl` grâce à `@Service`, crée le bean et lui fournit son `IContratRepository` par injection au constructeur.

### 2. Pourquoi un contrôleur dépendra-t-il de IContratService et non de ContratServiceImpl ?

Dépendre de l'interface réduit le couplage entre le contrôleur et l'implémentation concrète.

Il devient possible de remplacer l'implémentation du service, notamment lors des tests, sans modifier le contrôleur.

Cela respecte le principe de dépendance envers les abstractions.

### 3. Pourquoi un service singleton doit-il rester sans état ? Donner un exemple de bug.

Un service Spring est singleton par défaut : la même instance peut être utilisée simultanément par plusieurs requêtes.

S'il stocke des données propres à une requête dans un attribut modifiable, plusieurs utilisateurs risquent de partager ou d'écraser ces données.

Par exemple, un attribut `contratCourant` dans `ContratServiceImpl` pourrait être remplacé par la requête d'un autre utilisateur avant la fin du traitement.

Les données propres à chaque appel doivent donc rester dans les paramètres et variables locales des méthodes.

### 4. Pourquoi update charge-t-il l'entité existante avant d'appeler save au lieu de lui passer directement l'objet reçu ?

Charger l'entité existante permet de vérifier qu'elle existe et de modifier uniquement les champs autorisés.

Enregistrer directement l'objet reçu risquerait d'écraser des champs non transmis, de modifier des relations ou de perturber les collections associées.

La méthode `update` récupère donc l'entité, recopie les valeurs modifiables et appelle ensuite `save`.

### 5. Quelle différence entre @Component et @Bean ? Entre @Primary et @Qualifier ? Pourquoi IPaiementService n'expose-t-il ni création, ni modification, ni suppression ?

**`@Component` et `@Bean` :**

`@Component` est placé sur une classe pour permettre sa détection automatique par Spring.

`@Bean` est placé sur une méthode de configuration afin de déclarer explicitement l'objet que Spring doit gérer.

**`@Primary` et `@Qualifier` :**

`@Primary` indique l'implémentation prioritaire lorsqu'il existe plusieurs beans du même type.

`@Qualifier` désigne précisément le bean à injecter pour une dépendance donnée.

**`IPaiementService` :**

Il expose uniquement `findById` et `findAll` parce que les opérations de création, modification et suppression des paiements doivent passer par le Contrat, conformément aux règles de l'atelier.

Cela évite de modifier directement les paiements indépendamment de leur contrat.

---

## Conclusion

L'Atelier 4 met en place une couche Service distincte de la couche Repository, en utilisant l'inversion de contrôle et l'injection de dépendances par constructeur.

Cette architecture améliore la modularité, la maintenabilité et la testabilité du projet AutoLoc.

Les contrôleurs REST et les tests fonctionnels seront traités dans l'Atelier 5.