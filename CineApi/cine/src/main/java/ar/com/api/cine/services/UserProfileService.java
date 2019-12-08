package ar.com.api.cine.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import ar.com.api.cine.entities.UserProfile;
import ar.com.api.cine.repo.ProfileRepository;

public class UserProfileService {
    
        @Autowired
        ProfileRepository repo;
    
        @Autowired
        UserProfile up;
    
        public void save(UserProfile up) {
            repo.save(b);
        }
    
        public UserProfile SearchById(int id) {
    
            Optional<UserProfile> b = repo.findById(id);
    
            if (b.isPresent()) {
                return b.get();
            }
            return null;
        }
    
        public UserProfile findByProfile(UserProfile up) {
    
            return repo.findByProfile(up);
        }
    
        public BigDecimal consultarSaldo(int billeteraId, String moneda) {
            Billetera b = this.buscarPorId(billeteraId);
            BigDecimal s = new BigDecimal(0);
    
            for (Cuenta c : b.getCuentas()) {
    
                if (c.getMoneda().equals(moneda)) {
    
                    s = c.getSaldo();
                }
            }
    
            return s;
    
        }
    
        public Integer transferir(Integer billeteraIdOrigen, Integer billeteraIdDestino, BigDecimal importe) throws CuentaPorMonedaException
        
        {
            Billetera b1 = this.buscarPorId(billeteraIdOrigen);
            Billetera b2 = this.buscarPorId(billeteraIdDestino);
            Integer mov = b1.movimientoTransferir(importe.negate(), b1.getCuenta(0), b2.getCuenta(0));
            b2.movimientoTransferir(importe, b2.getCuenta(0), b1.getCuenta(0));
            repo.save(b1);
            repo.save(b2);
            
    
            es.SendEmail(b1.getPersona().getUsuario().getUserEmail(), "Transferencia realizada" +
            "La transferencia de" + importe +"Ha sido realizada con éxito!",
            "enhorabuena!!");
    
            
            es.SendEmail(b2.getPersona().getUsuario().getUserEmail(), "Transferencia realizada" +
            "Te transfirieron" + importe +"exitosamente!",
            "enhorabuena!!");
    
            return mov;
        }
    }
    
    
}