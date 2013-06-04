package controllers

import models.Motorcycle
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Action
import play.api.mvc.Controller
import anorm.NotAssigned
import anorm.Pk
import com.sun.xml.internal.bind.v2.TODO
import play.api.mvc.Result


object Motorcycles extends Controller{

    
   lazy val motorcycleMapping=mapping(
        // no binding for id.
        "id" -> ignored(NotAssigned:Pk[Long]),
        "make" -> nonEmptyText,
        "model" -> nonEmptyText,
        "engineCapacity" ->  number(50,Int.MaxValue, false)
        )(Motorcycle.apply)(Motorcycle.unapply)
         
 
   val motorcycleForm = Form[Motorcycle](motorcycleMapping)  
  
   
   def list = Action {
       val bikes = Motorcycle.getAll;
       Ok(views.html.list(Motorcycle.getAll))
   }
   
   def newMotorcycle = Action{
      Ok(views.html.add(motorcycleForm))
   } 
   
   def create = Action {
     implicit request =>
        val motorcycleFormNew = this.motorcycleForm.bindFromRequest;
        motorcycleFormNew.fold(
          hasErrors = {formWithError =>
                     Ok(views.html.add(formWithError)) },       
          success = validateCreate
       )
     
   }

  private def validateCreate(motorcycle: Motorcycle): Result = {
    // When adding a new motorcycle, it must not already exists in the db.     
    if (exists(motorcycle)) {
      val newErrorForm = motorcycleForm.fill(motorcycle).withGlobalError("Make and model already exists.")
      Ok(views.html.add(newErrorForm))

    } else {
      // Passed all validation. Add motorcycle to db.
      Motorcycle.insert(motorcycle);
      Redirect(routes.Motorcycles.list)
    }

  }
   
   def editMotorcycle(id:Long)= Action{
      implicit request =>
       val motorcycleOption = Motorcycle.findById(id)
        val bike = motorcycleForm.fill(motorcycleOption.get) 
         Ok(views.html.edit(bike)).withSession( session + ("motorcycleId" -> id.toString))
   }
   
   def update =  Action {
     implicit request =>
        val motorcycleFormNew = this.motorcycleForm.bindFromRequest;
        motorcycleFormNew.fold(
          hasErrors = {formWithError =>
                     Ok(views.html.edit(formWithError)) },     
                     
          success = {  motorcycle => { val id = session.get("motorcycleId").get.toLong
                                       Motorcycle.update(id,motorcycle);
                                       Redirect(routes.Motorcycles.list) }
          }
       )           
   }
   
    def delete(id:Long)=Action{       
         Motorcycle.delete(id); 
         Redirect(routes.Motorcycles.list)
    }
   
   
   private def exists(motorcycle:Motorcycle):Boolean ={
      Motorcycle.findByMakeAndModel(motorcycle) != None      
   }
}