if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}  

function showAllTasksInTable()
{
	  var jsonData;
	  $("#records_table tr").remove();
	  $.ajax({
	      url: '/getalltasksjson',
	      type: 'GET',
	      data: 
	      {
	      },
	      success: function (response) {
	    	  var trHTML= 
	    	    '<tr><td>' + 
	    	    'Id '+ '</td><td>' + 
				'Name' + '</td><td>' + 
				'Due Date' + 
				'</td></tr>';
	          $.each(response, function (i, item) 
	          {
	              //trHTML += '<tr><td>' + item.name + '</td></tr>';
	        	    trHTML += '<tr><td>' + item.id + '</td><td>' + 
	        	    					item.name + '</td><td>' + 
	        	    					item.dueDate + '</td></tr>';
	          });
	          $('#records_table').append(trHTML);
	      }
	  });
}

function addNewRandomTaskToDB()
{
	 $.ajax({
	      url: '/inserttaskandreturnjson',
	      type: 'POST',
	      data: 
	      {
	      },
	      success: function (response) {
	    	  showAllTasksInTable();
	      }
	  });
}

function deleteTaskById()
{
	 var id = $('#taskid').val();
	 $.ajax({
	      url: '/deletetask',
	      type: 'GET',
	      data: 
	      {
	    	  id:id
	      },
	      success: function (response) {
	    	  showAllTasksInTable();
	      }
	  });
}