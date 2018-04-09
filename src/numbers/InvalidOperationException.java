package numbers;

public class InvalidOperationException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 4070634332494345876L;
	
    /**
     * Constructs an <code>InvalidOperationException</code> with no detail message.
     */
    public InvalidOperationException () {
        super();
    }

    /**
     * Constructs an <code>InvalidOperationException</code> with the
     * specified detail message.
     *
     * @param   s   the detail message.
     */
    public InvalidOperationException (String s) {
        super (s);
    }

    /**
     * Factory method for making an <code>InvalidOperationException</code>
     * given the specified input which caused the error.
     *
     * @param   s   the input causing the error
     */
    static InvalidOperationException forInputString(String s) {
        return new InvalidOperationException("For input string: \"" + s + "\"");
    }
	
}
