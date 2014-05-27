package message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * oppa google style.
 */
public class AddressService {
    private List<Address> accountServices = new ArrayList<>();
    private List<Address> gameMechs = new ArrayList<>();

    public Address getAccountServiceAddress(){
        return accountServices.get(new Random().nextInt(accountServices.size()));
    }
    public Address getGameMechAddress(){
        return gameMechs.get(new Random().nextInt(gameMechs.size()));
    }

    public void setAccountServiceAddress(Address address){
        accountServices.add(address);
    }
    public void setGameMechAddress(Address address) {
        gameMechs.add(address);
    }

}
