/**
 *  colourpipe — Colourise program output
 *  Copyright © 2012  Mattias Andrée  (maandree@kth.se)
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
package se.kth.maandree.colourpipe.ecj;

import java.util.Scanner;


/**
 * The is the main class of the `ecj` colouriser
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Ecj
{
    /**
     * Non-constructor
     */
    private Ecj()
    {
	assert false : "You may not create instances of this class [Ecj].";
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
	    String line = sc.nextLine();
	    
	    if (sc.hasNextLine() == false)
	    {
		System.out.println("\033[36m" + line + "\033[39m");
		break;
	    }
	    if (line.equals("----------") && first)
	    {
		System.out.println("\033[1;30m" + line + "\033[21;39m");
		continue;
	    }
	    
	    String intro = line;
	    String code = sc.nextLine();
	    String mark = sc.nextLine();
	    String info = sc.nextLine();
	    
	    String num = intro.substring(0, intro.indexOf(' ') + 1);
	    intro = intro.substring(num.length());
	    final boolean warning = intro.startsWith("ERROR ") == false;
	    String type = intro.substring(0, intro.indexOf("in ") + 3);
	    intro = intro.substring(type.length());
	    String file = intro.substring(0, intro.lastIndexOf("(") - 1);
	    intro = intro.substring(file.length());
	    String preline = intro.substring(0, intro.lastIndexOf("line ") + 5);
	    intro = intro.substring(preline.length());
	    String linenum = intro.substring(0, intro.indexOf(")"));
	    String postline = intro.substring(linenum.length());
	    
	    System.out.print("\033[" + (warning ? "36" : "34") + "m" + num + type);
	    System.out.print("\033[35m" + file);
	    System.out.print("\033[" + (warning ? "36" : "34") + "m" + preline);
	    System.out.print("\033[32m" + linenum);
	    System.out.println("\033[" + (warning ? "36" : "34") + "m" + postline + "\033[39m");
	    
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
	    
	    System.out.println("\033[" + (warning ? "36" : "34") + "m" + info + "\033[39m");
	    while ((line = sc.nextLine()).equals("----------") == false)
		System.out.println("\033[1;30m" + line + "\033[21;39m");
	    System.out.println("\033[1;30m" + line + "\033[21;39m");
	}
    }
    
}
