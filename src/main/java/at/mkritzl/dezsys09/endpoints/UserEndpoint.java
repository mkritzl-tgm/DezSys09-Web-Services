package at.mkritzl.dezsys09.endpoints;

import at.mkritzl.dezsys09.data.Message;
import at.mkritzl.dezsys09.data.UserAccount;
import at.mkritzl.dezsys09.utils.UserAccountValidator;
import at.mkritzl.dezsys09.persistent.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Rest-Endpoint fuer die Registrierung und den Login des Useraccounts
 *
 * @author Martin Kritzl
 * @version 20160331
 */
@Path("/")
@Produces({MediaType.APPLICATION_JSON})
@Named
public class UserEndpoint {

    @Autowired
    private UserRepository repository;

    /**
     * Endpoint fuer die Registrierung des Useraccounts
     *
     * @param requestAccount Der Benutzer der registriert werden soll
     * @return Eine HTTP-Response, die eine Aussage ueber die erfolgreiche Registrierung des Benutzers gibt
     */
    @POST
    @Path("/register")
    public Response register(@Valid UserAccount requestAccount) {
        if (UserAccountValidator.hasData(requestAccount)) {
            //Alle Argumente vorhanden
            if (this.repository.findOne(requestAccount.getEmail()) == null) {
                //Anlegen des neuen Benutzers
                UserAccount account = new UserAccount(requestAccount.getEmail(), requestAccount.getPassword());
                this.repository.save(account);
                //Erfolgreiche Response schicken
                int status = Response.Status.CREATED.getStatusCode();
                return Response.status(status).entity(new Message(status, "Created account with email " + account.getEmail())).build();
            } else {
                //Der Benutzer ist bereits vorhanden
                int status = Response.Status.FORBIDDEN.getStatusCode();
                return Response.status(status).entity(new Message(status, "The email " + requestAccount.getEmail() + " is already used by another")).build();
            }
        } else {
            //Die Argumente sind invalid
            int status = Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(status).entity(new Message(status, "One or more account-arguments not set")).build();
        }
    }

    /**
     * Endpoint fuer den Login eines Useraccounts
     *
     * @param requestAccount Der Benutzer der angemeldet werden soll
     * @return Eine HTTP-Response, die eine Aussage ueber die erfolgreiche Anmeldung des Benutzers gibt
     */
    @POST
    @Path("/login")
    public Response login(@Valid UserAccount requestAccount) {
        if (UserAccountValidator.hasData(requestAccount)) {
            //Alle Argumente vorhanden
            UserAccount account = this.repository.findOne(requestAccount.getEmail());

            if (account != null && account.getPassword().equals(requestAccount.getPassword())) {
                //Erfolgreicher Login
                int status = Response.Status.OK.getStatusCode();
                return Response.status(status).entity(new Message(status, "Account with email " + account.getEmail() + " was logged in successfully")).build();
            } else {
                //Falsche Anmeldedaten
                int status = Response.Status.FORBIDDEN.getStatusCode();
                return Response.status(status).entity(new Message(status, "Invalid account credentials. Please try again")).build();
            }
        } else {
            //Die Argumente sind invalid
            int status = Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(status).entity(new Message(status, "Missing account argument or a required field was not set")).build();
        }
    }
}
