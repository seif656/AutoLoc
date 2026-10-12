package tn.esprit.tpfoyer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.domain.Reservation;
import tn.esprit.tpfoyer.exception.ResourceNotFoundException;
import tn.esprit.tpfoyer.repository.IReservationRepository;
import tn.esprit.tpfoyer.service.IReservationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;

    private void validate(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("La réservation est obligatoire");
        }
        if (reservation.getDateDebut() == null || reservation.getDateFin() == null
                || reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException("Les dates de réservation sont invalides");
        }
    }

    @Override
    public Reservation create(Reservation reservation) {
        validate(reservation);
        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException("Une nouvelle réservation ne doit pas avoir d'identifiant");
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        validate(reservation);
        Reservation existing = findById(id);
        existing.setDateDebut(reservation.getDateDebut());
        existing.setDateFin(reservation.getDateFin());
        existing.setStatut(reservation.getStatut());
        return reservationRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        reservationRepository.delete(findById(id));
    }
}