package error;

public final class ErrorManager {
	static final String[] errorMessages = {
		"Unknown Error", //0
		"Unable to Connect to Database", //1
		"No Arguments Provided", //2
		"Invalid Command", //3
		"Too Many Arguments Provided", //4
		"Requires One Argument", //5
		"Requires Two Arguments", //6
		"Requires Two or Three Arguments", //7
		"Invalid SQL Command", //8
		"Failed to Close Connection" //9
	};
	public static String getErrorMessage(int error) {
		try{
			return "Error "+error+": "+errorMessages[error];
		}
		catch(IndexOutOfBoundsException e) {
			return "";
		}
	}
}
