/** 
 * DNet eBusiness Suite
 * Copyright: 2013 Nan21 Electronics SRL. All rights reserved.
 * Use is subject to license terms.
 */
package net.nan21.dnet.core.api.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 
 * AMATHE: Copied from apache.commons.mail
 * 
 * Exception thrown when a checked error occurs in commons-email.
 * <p>
 * Supports nesting, emulating JDK 1.4 behavior if necessary.
 * <p>
 * Adapted from FunctorException in Commons Collections.
 *
 * @author jakarta-commons
 * @since 1.0
 * @version $Id: EmailException.java 510812 2007-02-23 04:32:22Z dion $
 */
public class EmailException
        extends Exception
{
    /** Serializable version identifier */
    static final long serialVersionUID = 5550674499282474616L;

    /**
     * Does JDK support nested exceptions?
     */
    private static final boolean JDK_SUPPORTS_NESTED;

    static
    {
        boolean flag = false;

        try
        {
            Throwable.class.getDeclaredMethod("getCause", new Class[0]);
            flag = true;
        }
        catch (NoSuchMethodException ex)
        {
            flag = false;
        }

        JDK_SUPPORTS_NESTED = flag;
    }

    /**
     * Root cause of the exception
     */
    private final Throwable rootCause;

    /**
     * Constructs a new <code>EmailException</code> with no
     * detail message.
     */
    public EmailException()
    {
        super();
        this.rootCause = null;
    }

    /**
     * Constructs a new <code>EmailException</code> with specified
     * detail message.
     *
     * @param msg  the error message.
     */
    public EmailException(String msg)
    {
        super(msg);
        this.rootCause = null;
    }

    /**
     * Constructs a new <code>EmailException</code> with specified
     * nested <code>Throwable</code> root cause.
     *
     * @param rootCause  the exception or error that caused this exception
     *                   to be thrown.
     */
    public EmailException(Throwable rootCause)
    {
        super((rootCause == null) ? null : rootCause.getMessage());
        this.rootCause = rootCause;
    }

    /**
     * Constructs a new <code>EmailException</code> with specified
     * detail message and nested <code>Throwable</code> root cause.
     *
     * @param msg  the error message.
     * @param rootCause  the exception or error that caused this exception
     *                   to be thrown.
     */
    public EmailException(String msg, Throwable rootCause)
    {
        super(msg);
        this.rootCause = rootCause;
    }

    /**
     * Gets the cause of this throwable.
     *
     * @return  the cause of this throwable, or <code>null</code>
     */
    public Throwable getCause()
    {
        return rootCause;
    }

    /**
     * Prints the stack trace of this exception to the standard error stream.
     */
    public void printStackTrace()
    {
        printStackTrace(System.err);
    }

    /**
     * Prints the stack trace of this exception to the specified stream.
     *
     * @param out  the <code>PrintStream</code> to use for output
     */
    public void printStackTrace(PrintStream out)
    {
        synchronized (out)
        {
            PrintWriter pw = new PrintWriter(out, false);
            printStackTrace(pw);

            // Flush the PrintWriter before it's GC'ed.
            pw.flush();
        }
    }

    /**
     * Prints the stack trace of this exception to the specified writer.
     *
     * @param out  the <code>PrintWriter</code> to use for output
     */
    public void printStackTrace(PrintWriter out)
    {
        synchronized (out)
        {
            super.printStackTrace(out);

            if (!JDK_SUPPORTS_NESTED && (rootCause != null))
            {
                out.print("Caused by: ");
                rootCause.printStackTrace(out);
            }
        }
    }
}
