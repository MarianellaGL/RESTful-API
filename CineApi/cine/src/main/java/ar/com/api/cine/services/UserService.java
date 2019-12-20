package ar.com.api.cine.services;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import ar.com.api.cine.entities.User;
import ar.com.api.cine.entities.UserProfile;
import ar.com.api.cine.repo.UserRepository;
import ar.com.api.cine.security.crypto.Crypto;

@Service
public class UserService {

    @Autowired
    UserRepository repo;

    @Autowired
    // EmailService emailService;

    public ObjectId create(String fullName, String dni, String email, String username, int edad, String password,
            String userEmail, String alias, String previousPurchases) throws UserException {

        User u = new User();
        u.setFullName(fullName);
        u.setEmail(email);
        u.setUsername(username);

        String passwordEnTextoClaro;
        String passwordEncriptada;
        String passwordEnTextoClaroDesencriptado;

        passwordEnTextoClaro = password;
        passwordEncriptada = Crypto.encrypt(passwordEnTextoClaro, u.getUsername());

        u.setPassword(passwordEncriptada);

        UserProfile up = new UserProfile();
        up.setAlias(alias);
        up.setPreviousPurchases(previousPurchases);

        up.grabar();

        return u.get_id();

    }

    public void save(User u) {
        repo.save(u);
    }

    public List<User> getUsuarios() {

        return repo.findAll();
    }

    public User searchByUsername(String username) {

        return repo.findByUsername(username);
    }

    public User buscarPorId(ObjectId id) {

        Optional<User> u = repo.findById(id);

        if (u.isPresent())
            return u.get();
        return null;
    }

    public void login(String username, String password) {

        User u = repo.findByUsername(username);

        if (u == null || !u.getPassword().equals(Crypto.encrypt(password, u.getUsername()))) {

            throw new BadCredentialsException("usuario o contraseña inválida");

        }
    }

    public User buscarPorEmail(String email) {
        return repo.findByEmail(email);
    }

}
