/**
 * Created with IntelliJ IDEA.
 * User: Koji Kuwana <koji.kuwana@gmail.com>
 * Date: 2013/10/19
 * Time: 14:35
 */
class GTailTest {

    def static main(args) {
        def tail = new GTail()
        def options = [
                "--encoding",
                "Cp1252",
                "-n",
                "5",
                "/Users/koji/IdeaProjects/gtail/test/data/de_cp1252.txt"
        ]
        def result = tail.execute(options)
        assert result.size() == 3
    }
}
