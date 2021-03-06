package scalaadventutils

import org.scalatest.funsuite.AnyFunSuite
import scala.collection.mutable.ArrayBuffer

class GridSpec extends AnyFunSuite {
    val grid = List[String](
        "#.#",
        ".#.",
        "#.#"
    )

    test("Grid: checkBounds") {
        val width = 2
        val height = 2
        val arr = ArrayBuffer(true, false, false, true)

        assert(arr.size == width * height)

        val grid = new Grid(arr, width, height)

        assert( grid.checkBounds(0 , 0))
        assert(!grid.checkBounds(-1, 0))
        assert( grid.checkBounds(1 , 1))
        assert(!grid.checkBounds(4 , 1))

        assert(!grid.checkBounds(width, height))
    }

    test("Grid: nonDiagNeighbours") {
        val grid1 = GridUtils.from2DCharArray(grid, '#')

        assertResult(4) {
            grid1.nonDiagNeighbours(0, 0).size
        }

        assertResult(4) {
            grid1.nonDiagNeighbours(1, 1).size
        }

        assertResult(4) {
            grid1.nonDiagNeighbours(2, 1).size
        }

        assertResult(2) {
            grid1.nonDiagNeighbours(0, 0, false).size
        }

        assertResult(4) {
            grid1.nonDiagNeighbours(1, 1, false).size
        }

        assertResult(3) {
            grid1.nonDiagNeighbours(2, 1, false).size
        }
    }

    test("Grid: get") {
        val grid1 = GridUtils.from2DCharArray(grid, '#')

        assert( grid1.get(0, 0))
        assert(!grid1.get(0, 1))
        assert(!grid1.get(1, 0))

        assert( grid1.get(3, 0))
        assert( grid1.get(3, 3))
        assert(!grid1.get(7, 0))

        assert( grid1.get(-1, 0))

        assert(grid.mkString("\n") == grid1.toString())
    }

    test("Grid: step") {
        var grid1 = GridUtils.from2DCharArray(grid, '#')

        def stepFn(x: Int, y: Int): Boolean = {
            x <= 1 && y <= 1
        }

        grid1 = grid1.step(stepFn)

        val grid2 = List[String](
            "##.",
            "##.",
            "..."
        )

        assert(grid2.mkString("\n") == grid1.toString())
    }

    test("Grid: flip and rotate") {
        val g = List[String](
            "##.",
            "##.",
            "..#"
        )

        val flipped = List[String](
            ".##",
            ".##",
            "#.."
        )

        var grid = GridUtils.from2DCharArray(g, '#')

        assert(grid.flip.toString() == flipped.mkString("\n"))

        val rotated = List[String](
            "#..",
            ".##",
            ".##"
        )

        assert(grid.rotate.rotate.toString() == rotated.mkString("\n"))

        assert(grid.transformations.size == 8)
        assert(grid.transformations.distinct.size == 4)

        assert(grid.flip.equals(grid.rotate.rotate.rotate))
    }

    test("Grid: split") {
        val g = List[String](
            "#..#",
            "....",
            "....",
            "#..#"
        )

        var grid = GridUtils.from2DCharArray(g, '#')

        assert(grid.split(2).size == 4)

        val g2 = List[String](
            "##.##.",
            "#..#..",
            "......",
            "##.##.",
            "##.#..",
            "......"
        )

        var grid2 = GridUtils.from2DCharArray(g2, '#')

        assert(grid2.split(3).size == 4)
        assert(grid2.split(2).size == 9)
    }

    test("GridUtils: join") {
        val g = List[String](
            "#..#",
            "....",
            "....",
            "#..#"
        )

        var grid = GridUtils.from2DCharArray(g, '#')

        assert(GridUtils.join(grid.split(2)) == grid)

        val g2 = List[String](
            "##.##.",
            "#..#..",
            "......",
            "##.##.",
            "##.#..",
            "......"
        )

        var grid2 = GridUtils.from2DCharArray(g2, '#')

        assert(GridUtils.join(grid2.split(2)) == grid2)
        assert(GridUtils.join(grid2.split(3)) == grid2)
    }

    test("GridUtils: from Char Array") {
        val g  = "...#".toCharArray
        val g1 = List[String](
            "..",
            ".#",
        )

        val grid  = GridUtils.from1DCharArray(g, '#')
        val grid1 = GridUtils.from2DCharArray(g1, '#')

        assert(grid.toString() == grid1.toString())
    }
}

