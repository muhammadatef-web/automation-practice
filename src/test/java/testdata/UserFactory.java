package testdata;

import core.config.ConfigReader;
import java.util.UUID;

public final class UserFactory {

    public record UserAccount(String name, String email, String password) {}

    private UserFactory() {}

    public static UserAccount freshValidUser() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return new UserAccount(
                ConfigReader.get("valid.name"),
                "muhammad.qa." + uniqueId + "@example.com",
                ConfigReader.get("valid.password")
        );
    }

    public static UserAccount invalidPasswordUser() {
        return new UserAccount(
                ConfigReader.get("valid.name"),
                ConfigReader.get("valid.email"),
                "WrongPass@999"
        );
    }

    public static UserAccount unregisteredUser() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return new UserAccount(
                "Unknown User",
                "unregistered." + uniqueId + "@example.com",
                "WrongPass@999"
        );
    }
}