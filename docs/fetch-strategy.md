
# JPA Fetch Strategy and Cascade Configuration

## Project: AutoLoc — Atelier 2

## 1. Relationships Overview

| Relationship | Type | Owning Side | Fetch | Cascade |
|---|---|---|---|---|
| Contrat ↔ Paiement | OneToMany / ManyToOne | Paiement | LAZY | ALL + orphanRemoval on Contrat |
| Agence ↔ Vehicule | OneToMany / ManyToOne | Vehicule | LAZY | None |
| Agence ↔ Employe | OneToMany / ManyToOne | Employe | LAZY | None |
| Vehicule ↔ Equipement | ManyToMany | Vehicule | LAZY | None |
| Client ↔ Reservation | OneToMany / ManyToOne | Reservation | LAZY | PERSIST on Client |
| Reservation ↔ Vehicule | ManyToOne / OneToMany | Reservation | LAZY | None |
| Reservation ↔ Contrat | OneToOne | Contrat | LAZY | ALL on Reservation |
| Vehicule ↔ Maintenance | OneToMany / ManyToOne | Maintenance | LAZY | PERSIST on Vehicule |

## 2. Fetch Strategy

FetchType.LAZY is used for all relationships to avoid loading unnecessary associated entities.

LAZY loading improves performance by retrieving related data only when accessed within an active persistence context.

FetchType.EAGER was tested for the Contrat-Paiement relationship during the guided exercise. The final configuration uses LAZY.

## 3. Cascade Strategy

### Contrat - Paiement

CascadeType.ALL propagates persistence, updates and deletions from Contrat to Paiement.

orphanRemoval = true removes a payment when it is removed from the contract's collection.

### Agence - Vehicule and Agence - Employe

No cascade is configured because vehicles and employees have independent lifecycles.

### Vehicule - Equipement

No cascade is configured because equipment may be shared by multiple vehicles.

### Client - Reservation

CascadeType.PERSIST allows new reservations to be persisted when their client is persisted.

### Reservation - Vehicule

No cascade is configured because deleting or saving a reservation should not automatically affect the associated vehicle.

### Reservation - Contrat

CascadeType.ALL is configured on Reservation to propagate persistence, updates and deletions to its associated contract.

### Vehicule - Maintenance

CascadeType.PERSIST allows new maintenance records to be persisted when their vehicle is persisted.

## 4. Database Structure

The following foreign keys are managed by Hibernate:

- paiement → contrat
- vehicule → agence
- employe → agence
- reservation → client
- reservation → vehicule
- contrat → reservation
- maintenance → vehicule

The ManyToMany relationship between Vehicule and Equipement uses the junction table vehicule_equipement.

