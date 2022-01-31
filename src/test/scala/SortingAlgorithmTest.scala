import org.junit.Assert._
import org.junit.Test
import poemGenerator.controllers.InputController

class SortingAlgorithmTest {
  val inputController = new InputController();

  @Test
  def sortingList1 {
    val result = (inputController.iterateSorted2dArray(20, Array(15, 3, 18), "start").toList).map(res => res.toList)
    val expectedResult = (Array(
      Array(15, 3, 18),
      Array(15, 4, 18),
      Array(15, 4, 3),
      Array(16, 4, 3),
      Array(16, 5, 3),
      Array(16, 5, 4),
      Array(17, 5, 4),
      Array(17, 6, 4),
      Array(17, 6, 6),
      Array(19, 6, 6),
      Array(19, 8, 6),
      Array(19, 8, 8),
      Array(20, 8, 8),
      Array(20, 9, 8),
      Array(20, 9, 9),
      Array(1, 9, 9),
      Array(1, 10, 9),
      Array(1, 10, 10),
      Array(2, 10, 10),
      Array(2, 1, 10)).toList).map(res => res.toList)

    assertEquals(result, expectedResult)
  }

  @Test
  def sortingList2 {
    val result = (inputController.iterateSorted2dArray(20, Array(19, 7, 4), "start").toList).map(res => res.toList)
    val expectedResult = (Array(
      Array(19, 7, 4),
      Array(19, 8, 4),
      Array(19, 8, 5),
      Array(20, 8, 5),
      Array(20, 9, 5),
      Array(20, 9, 6),
      Array(2, 9, 6),
      Array(2, 4, 6),
      Array(2, 4, 7),
      Array(4, 4, 7),
      Array(4, 5, 7),
      Array(4, 5, 8),
      Array(5, 5, 8),
      Array(5, 7, 8),
      Array(5, 7, 9),
      Array(6, 7, 9),
      Array(6, 8, 9),
      Array(6, 8, 10),
      Array(7, 8, 10),
      Array(7, 9, 10)).toList).map(res => res.toList)

    assertEquals(result, expectedResult)
  }

  @Test
  def sortingList3 {
    val result = (inputController.iterateSorted2dArray(20, Array(19, 7, 4), "end").toList).map(res => res.toList)
    val expectedResult = (Array(
      Array(7, 9, 10),
      Array(7, 8, 10),
      Array(6, 8, 10),
      Array(6, 8, 9),
      Array(6, 7, 9),
      Array(5, 7, 9),
      Array(5, 7, 8),
      Array(5, 5, 8),
      Array(4, 5, 8),
      Array(4, 5, 7),
      Array(4, 4, 7),
      Array(2, 4, 7),
      Array(2, 4, 6),
      Array(2, 9, 6),
      Array(20, 9, 6),
      Array(20, 9, 5),
      Array(20, 8, 5),
      Array(19, 8, 5),
      Array(19, 8, 4),
      Array(19, 7, 4)).toList).map(res => res.toList)

    assertEquals(result, expectedResult)
  }

}
