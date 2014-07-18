package models

import play.api.db.DB
import anorm.Pk
import anorm.NotAssigned
import anorm.RowParser

case class Motorcycle(id: Pk[Long] = NotAssigned, make: String, model: String, engineCapacity: Int)

/**
 * A sort DAO for for the case class Motorcycle.
 */
object Motorcycle {

  import anorm.SQL
  import anorm.SqlQuery
  import anorm.ResultSetParser

  import play.api.Play.current

  def getAll: List[Motorcycle] = DB.withConnection {
    implicit connection =>
      val sql: SqlQuery = SQL("select * from motorcycles order by make,model asc")
      sql.as(motorcyclesParser)

  }

  def insert(motorcycle: Motorcycle): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""insert
            into motorcycles
            values (
            (select nextval('motorcycles_seq')), 
            {make}, {model}, {engineCapacity})""").on(
        "make" -> motorcycle.make,
        "model" -> motorcycle.model,
        "engineCapacity" -> motorcycle.engineCapacity).executeUpdate() == 1
    }
  }

  def update(id: Long, motorcycle: Motorcycle): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""update motorcycles
            set make = {make}, model = {model}, engineCapacity = {engineCapacity}
            where id = {id}""").on(
        "make" -> motorcycle.make,
        "model" -> motorcycle.model,
        "engineCapacity" -> motorcycle.engineCapacity,
        "id" -> id)
        .executeUpdate() == 1
    }
  }

  def findByMakeAndModel(motorcycle: Motorcycle): Option[Motorcycle] =
    DB.withConnection { implicit connection =>
      val sql = SQL("select * from motorcycles where make={make} and model={model}").
        on("make" -> motorcycle.make,
          "model" -> motorcycle.model)

      sql.as(motorcyclesParser).headOption
    }

  def findById(id: Long): Option[Motorcycle] = {
    DB.withConnection { implicit connection =>
      val sql = SQL("select * from motorcycles where id = {id}").on('id -> id);
      sql.as(motorcyclesParser).headOption
    }
  }

  def delete(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL("delete from motorcycles where id = {id}").on('id -> id).executeUpdate()
    }
  }

  val motorcycleParser: RowParser[Motorcycle] = {
    import anorm.~
    import anorm.SqlParser._
    get[Pk[Long]]("id") ~
      str("make") ~
      str("model") ~
      int("engineCapacity") map {
        case id ~ make ~ model ~ engineCapacity =>
          Motorcycle(id, make, model, engineCapacity)
      }
  }

  val motorcyclesParser: ResultSetParser[List[Motorcycle]] = {
    motorcycleParser *
  }

}






