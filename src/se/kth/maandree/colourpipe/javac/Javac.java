/**
 * colourpipe — Colourise program output
 * Copyright © 2012, 2013  Mattias Andrée  (maandree@member.fsf.org)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.kth.maandree.colourpipe.javac;

import java.util.Scanner;


/**
 * The is the main class of the `javac` colouriser
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Javac
{
    /**
     * Non-constructor
     */
    private Javac()
    {
	assert false : "You may not create instances of this class [Javac].";
    }
    
    
    
    /**
     * This is the main entry point of the program
     * 
     * @param  args  Startup arguments, unused
     */
    public static void main(final String... args)
    {
	boolean first = true, end = false;
	final Scanner sc = new Scanner(System.in);
	while (sc.hasNextLine())
	{
	    final String line = sc.nextLine();
	    
	    if (line.startsWith("  "))
	    {
		System.out.println("\033[1;30m" + line + "\033[21;39m");
		continue;
	    }
	    
	    if (line.equals("1 warning") || line.equals("1 error"))
	    {
		if (end == false)
		    System.out.println();
		end = true;
		System.out.println("\033[36m" + line + "\033[39m");
		continue;
	    }
	    else
	    {   String tmp = line;
		if      (tmp.endsWith(" errors"))    tmp = tmp.substring(0, tmp.lastIndexOf(" "));
		else if (tmp.endsWith(" warnings"))  tmp = tmp.substring(0, tmp.lastIndexOf(" "));
		try
		{   Integer.parseInt(tmp);
		    if (end == false)
			System.out.println();
		    end = true;
		    System.out.println("\033[36m" + line + "\033[39m");
		    continue;
		}
		catch (final Throwable err)
		{   // resume
		}
	    }
	    
	    if (first)
		first = false;
	    else
		System.out.println();
	    
	    System.out.print("\033[35m");
	    System.out.print(line.split(":")[0]);
	    System.out.print("\033[36m:\033[32m");
	    System.out.print(line.split(":")[1]);
	    System.out.print("\033[36m:");
	    final String message = line.substring(line.indexOf(' '));
	    final boolean warning = message.startsWith(" warning: ");
	    System.out.print((warning ? "\033[36m" : "\033[34m") + message);
	    System.out.println("\033[39m");
	    
	    String code = sc.nextLine();
	    String mark = sc.nextLine();
	    
	    while (mark.replace(" ", "").replace("\t", "").replace("^", "").isEmpty() == false)
	    {
		System.out.println("\033[1;30m" + code + "\033[21;39m");
		code = mark;
		mark = sc.nextLine();
	    }
	    
	    int colour = 0;
	    int i = 0, j = 0;
	    while ((i < code.length()) && (j < mark.length()))
	    {
		String c = Character.toString(code.charAt(i++));
		String m = Character.toString(mark.charAt(j++));
		if ((c.charAt(0) & 0xDC00) == 0xD800)  c += code.charAt(i++);
		if ((m.charAt(0) & 0xDC00) == 0xD800)  m += mark.charAt(i++);
		
		final boolean cm = m.equals("^");
		final boolean cw = c.equals(" ");
		final int newColour = cm ? cw ? 2 : 1 : 0;
		
		if (newColour == 2)
		{   System.out.print("\033[35m_");
		}
		else if (newColour == 1)
		{   if (colour == 1)
		    {   System.out.print(c);
		    }
		    else
		    {	System.out.print((warning ? "\033[33m" : "\033[31m") + c);
		}   }
		else if (colour == 0)
		{   System.out.print(c);
		}
		else
		{   System.out.print("\033[39m" + c);
		}
		colour = newColour;
	    }
	    if (i < code.length())
		System.out.print("\033[39m" + code.substring(i));
	    while (j < mark.length())
	    {
		String m = Character.toString(mark.charAt(j++));
		if ((m.charAt(0) & 0xDC00) == 0xD800)  m += mark.charAt(i++);
		
		final boolean cm = m.equals("^");
		
		if (colour == 3)  System.out.print('.');
		else              System.out.print("\33[35m.");
		colour = 3;
	    }
	    
	    System.out.println("\033[39m");
	}
    }
    
}
