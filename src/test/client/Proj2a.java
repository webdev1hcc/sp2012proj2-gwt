package test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.core.client.JsonUtils;
import java.util.ArrayList;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Proj2a implements EntryPoint
{
   VerticalPanel mainPanel = new VerticalPanel();
   String baseURL = "http://localhost:3000";
   ArrayList<MyStudent> students =
      new ArrayList<MyStudent>();
   JsArray<Student> jsonData;
   
   private static class MyStudent
   {
      private int id;
      private String first_name;
      private String last_name;
      private String major;
      
      public MyStudent(int id, String fn, String ln, String maj)
      {
         this.id = id;
         this.first_name = fn;
         this.last_name = ln;
         this.major = maj;
      }
   }
   public void onModuleLoad()
   {
      String url = baseURL + "/students/index.json";
      getRequest(url,"getStudents");
      RootPanel.get().add(mainPanel);
   }
   public void getRequest(String url, final String getType) {
      final RequestBuilder rb = new
         RequestBuilder(RequestBuilder.GET,url);
      try {
         rb.sendRequest(null, new RequestCallback()
         {
            public void onError(final Request request,
               final Throwable exception)
            {
               Window.alert(exception.getMessage());
            }
            public void onResponseReceived(final Request request,
               final Response response)
            {
               if (getType.equals("getStudents")) {
                  showStudents(response.getText());
               }
            }
         });
      }
      catch (final Exception e) {
         Window.alert(e.getMessage());
      }
   } // end getRequest()
   private void showStudents(String responseText)
   {
      jsonData = getData(responseText);
      Student student = null;
      for (int i = 0; i < jsonData.length(); i++) {
         student = jsonData.get(i);
         students.add(new MyStudent(student.getID(),
            student.getFirstName(), student.getLastName(),
            student.getMajor()));
      }
      CellTable<MyStudent> table = new CellTable<MyStudent>();
      TextColumn<MyStudent> fnameCol = 
         new TextColumn<MyStudent>()
         {
            @Override
            public String getValue(MyStudent student)
            {
               return student.first_name;
            }
         };
      TextColumn<MyStudent> lnameCol = 
         new TextColumn<MyStudent>()
         {
            @Override
            public String getValue(MyStudent student)
            {
               return student.last_name;
            }
         };
      
      table.addColumn(fnameCol, "First Name");
      table.addColumn(lnameCol, "Last Name");
      table.setRowCount(students.size(),true);
      table.setRowData(0,students);
      mainPanel.add(table);
   } // end showStudents()
   private JsArray<Student> getData(String json)
   {
      return JsonUtils.safeEval(json);
   }
}