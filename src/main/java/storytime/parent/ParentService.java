package storytime.parent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storytime.common.CrudService;

import java.util.Optional;

@Service
public class ParentService extends CrudService<Parent, ParentRepository> {
  public static final String LOGIN_FAIL_MESSAGE =
      "Either the username doesn't exist or passphrase is incorrect";
  private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

  @Autowired
  public ParentService(ParentRepository parentRepository) {
    super(parentRepository);
  }

  public Optional<String> loginAttempt(Parent attempted) {
    String username = attempted.getUsername();
    String passphrase = attempted.getPassphrase();

    // lookup the actual username/passphrase to auth
    Optional<Parent> actual = repository.findByUsername(username).stream().findFirst();

    // username doesn't exist or passphrase doesn't match
    if (!actual.isPresent()) {
      log.info("{} not found", username);
      return Optional.of(LOGIN_FAIL_MESSAGE);
    } else if (!actual.get().getPassphrase().equals(passphrase)) {
      log.info("{} != {}", passphrase, actual.get().getPassphrase());
      return Optional.of(LOGIN_FAIL_MESSAGE);
    }
    return Optional.empty();
  }

  public Optional<Long> getIdForUsername(String username) {
    log.info("getIdForUsername({})", username);

    // find the parent with the given username
    // if found return its Id
    // else return empty optional Long
    return repository.findByUsername(username).stream().findFirst().map(Parent::getId);
  }
}
