package error;

public final class ErrorManager {
	static final String[] errorMessages = {
		"Unknown Error", //0
		"Unable to Connect to Database", //1
		//Trying to access non-existent table
		//Missing semicolon
		//etc.
	};
	public String getErrorMessage(int error) {
		try{
			return "Error "+error+": "+errorMessages[error];
		}
		catch(IndexOutOfBoundsException e) {
			return "";
		}
	}
}
