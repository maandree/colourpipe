all: javac ecj

javac:
	mkdir -p META-INF
	javac -cp . -s src -d . src/se/kth/maandree/colourpipe/*/*.java
	echo -e 'Manifest-Version: 1.0\nCreated-By: Mattias Andrée\nMain-Class: se.kth.maandree.colourpipe.javac.Javac' > META-INF/MANIFEST.MF
	jar -cfm colourpipe.javac.jar META-INF/MANIFEST.MF se/kth/maandree/colourpipe/javac/*.class
	echo "java -jar "$$\0".jar" > colourpipe.javac
	echo "(javac "$$\@" |& java -jar /usr/bin/colourpipe.javac.jar)" > javac-colour
	rm -r META-INF se
	rm *~ 2>/dev/null || true

ecj:
	mkdir -p META-INF
	javac -cp . -s src -d . src/se/kth/maandree/colourpipe/*/*.java
	echo -e 'Manifest-Version: 1.0\nCreated-By: Mattias Andrée\nMain-Class: se.kth.maandree.colourpipe.ecj.Ecj' > META-INF/MANIFEST.MF
	jar -cfm colourpipe.ecj.jar META-INF/MANIFEST.MF se/kth/maandree/colourpipe/ecj/*.class
	echo "java -jar "$$\0".jar" > colourpipe.ecj
	echo "(ecj "$$\@" |& java -jar /usr/bin/colourpipe.ecj.jar)" > ecj-colour
	rm -r META-INF se
	rm *~ 2>/dev/null || true

install: all
	install -d "${DESTDIR}/usr/bin"
	install -m 755 colourpipe.* *-colour "${DESTDIR}/usr/bin"

uninstall:
	rm -f ${DESTDIR}/usr/bin/colourpipe.*
	rm -f ${DESTDIR}/usr/bin/{javac,ecj}-colour
