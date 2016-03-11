package controllers;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import models.Task;
import crud.TaskManager;	
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import tools.RandomLongIdGenerator;
/**
 * This controller contains an action to handle HTTP requests for Tasks CRUD operations
 */
public class TaskController extends Controller{

	@Inject
	TaskManager taskManager;
	@Inject 
	RandomLongIdGenerator randomLongGenerator;
	
	public Result getCrudUIPage()
	{
	    return ok(views.html.cruduipage.render("test"));    
	}
	
    public Result insertTask()
    {
    	Task task = new Task();
    	task.id=new Long(randomLongGenerator.generateRandomLongValue());
    	task.dueDate=new Date();
    	task.name="test";
    	taskManager.insertTask(task);
    	return ok("Saved Task object id : " + task.id);
    }
    
    public Result insertTaskAndReturnAsJSON()
    {
    	Task task = new Task();
    	task.id=new Long(randomLongGenerator.generateRandomLongValue());
    	task.dueDate=new Date();
    	task.name="test";
    	taskManager.insertTask(task);
    	JsonNode json = Json.toJson(task);
    	return ok(json);
    }
    
    public Result getAllTasksPage()
    {
    	List<Task> allTasks = taskManager.getAllTasks();
    	return ok(views.html.tasks.render(returnTasksIdsAsString(allTasks)));
    }
    
    public Result getTaskByIdAsJSON(String id)
    {
    	Task selectedTask = taskManager.getTaskById(Long.valueOf(id));
    	JsonNode json = Json.toJson(selectedTask);
    	Logger.debug("TaskController.getTaskAsJSON");
    	Logger.debug(json.toString());
    	return ok(json);
    	
    }
    
    public Result getAllTasksJSON()
    {
    	List<Task> allTasks = taskManager.getAllTasks();
    	JsonNode json = Json.toJson(allTasks);
    	return ok(json);
    }
    
    private static String returnTasksIdsAsString(List<Task> tasksList)
    {
    	StringBuilder sb = new StringBuilder();
    	for(Task currentTask : tasksList)
    	{
    		sb.append(currentTask.id+",");
    	}
    	return sb.toString();	
    }
    
    public Result deleteTask(String id)
    {	
    	Long taskId = Long.valueOf(id);
    	taskManager.deleteTask(taskId);
    	return ok("Task with id : " + id + " deleted properly");
    }
  
}
