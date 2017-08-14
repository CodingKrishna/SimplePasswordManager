package utility;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordGenerator {
	
	public static int getPasswordStrength(String password) {
		int strengthPercentage = 0;
		String[] regexChecks = { "(?=.*[0-9])", //  a digit must occur at least once
		        "(?=.*[a-z])", // a lower case letter must occur at least once
		        "(?=.*[A-Z])" , // an upper case letter must occur at least once
		        "(?=.*[@#$%^&+=])", // a special character must occur at least once
		        "(?=\\S+$)", // no whitespace allowed in the entire string
		        ".{8,}", // anything, at least 8 places though
		        ".{16,}", // anything, at least 16 places though
		        ".{32,}", // ... 32
		        ".{64,}", // .. 65
		        ".{128,}" // .. 128
		};
		
		int n = regexChecks.length;
		int increase = 100/n;
		for( int i = 0; i < n; i++){
			Pattern p = Pattern.compile(regexChecks[i]);
			Matcher m = p.matcher(password);
			if (m.find())
				strengthPercentage+=increase;
        }

		return strengthPercentage;
	}
	
	public static String generateStrongPassword(int length){
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
		
		if (length <= 0)
            throw new IllegalArgumentException("Invalid string length!");

        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
	}
	
	public static String strengthConverter( int strength ){
		
		switch( strength ){
			case 0:
				return "stupidly weak";	
			case 10:
				return "absurdly weak";	
			case 20:
				return "very weak";		
			case 30:
				return "weak";	
			case 40:
				return "regular";	
			case 50:
				return "okay";	
			case 60:
				return "good";	
			case 70:
				return "very good";	
			case 80:
				return "really good";
			case 90:
				return "insane";
			case 100:
				return "waste of time";	
		}
		
		return "nil";
	}
	
}
