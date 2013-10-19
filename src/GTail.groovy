/**
 * GTail
 */
class GTail {

    def sourceEncoding = "UTF-8"
    def defaultLastLines = 10

    def static main(args) {
        if (args == null || args.length == 0) {
            println("Please specify a file name")
            return
        }
        GTail tail = new GTail();
        tail.execute(args.toList());
    }

    /**
     * Initialize CliBuilder
     * @return CliBuilder object
     */
    def CliBuilder initCliBuilder(){
        def cli = new CliBuilder(usage:"groovy GTail.groovy <OPTION>... [FILE]")
        cli.h(longOpt:'help', 'display this help')
        cli.c(longOpt:'encoding', argName:'charset', args:1, 'specify the encoding of the log files (ex UTF-8, Cp1252)')
        cli.n(longOpt:'lines', argName:'line number', args:1, 'output the last N lines, instead of the last 10')
        cli.f(longOpt:'follow', 'this programm watch the log file')
        cli
    }

    def execute(List args) {

        def cli = initCliBuilder()
        def options = cli.parse(args)

        // help
        if(options.h) {
            cli.usage()
            return
        }

        // charset
        if(options.c) {
            sourceEncoding = options.c
        }

        // line number for display
        if(options.n) {
            defaultLastLines = Integer.parseInt(options.n)
        }

        def path = args[-1];
        def file = new File(path);
        if (!file.exists()) {
            println("File does not exist.(${file.name})");
            return;
        }

        try {
            // execute tail
            List result = tail(file, defaultLastLines)

            // follow?
            if(options.f) {
                follow(file, file.length())
            } else {
                result
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    def List tail(File file, Integer initialPrint = defaultLastLines) {
        def CR = 0X0D
        def LF = 0x0A

        def length = file.length()
        def raf = new RandomAccessFile(file, "r")
        def pointer = length - 1

        def count = 0;
        def result = []
        def line = []
        def isBeforeLF = false
        while (pointer > -1 && count < initialPrint) {
            raf.seek(pointer)
            def c = raf.readByte()
            //def c = raf.readChar()
            //def c = raf.read()
            if (c == LF) {
                result.add(line.reverse())
                line = []
                count += 1
                isBeforeLF = true
            } else {
                if(isBeforeLF && c == CR)
                {
                    // ignore CR
                } else {
                    line.add(c)
                }
                isBeforeLF = false
            }
            pointer -= 1
        }

        if(pointer <= -1) {result.add(line.reverse())}
        def converted = result.reverse().collect {
            def bytes = (byte[])it
            new String(bytes, sourceEncoding)
        }
        converted.each {println it}

        // seek to last position of the end of log file.
        raf.seek(file.length())
        converted
    }

    def follow(File file, Long filePointer){
        while (true) {
            try {
                Thread.sleep(1000);
                def length = file.length();
                if (length < filePointer) {
                    println ("File has been initialized.")
                    filePointer = length;
                } else if (length > filePointer) {
                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                    raf.seek(filePointer);
                    String line;
                    while ((line = raf.readLine()) != null) {
                        def a = (byte[])line
                        if (line) println new String(a, sourceEncoding)
                    }
                    filePointer = raf.getFilePointer();
                    raf.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}