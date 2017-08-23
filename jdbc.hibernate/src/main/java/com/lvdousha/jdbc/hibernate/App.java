package com.lvdousha.jdbc.hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

public class App 
{
    public static void main( String[] args ){
    	App app = new App();
    	app.createTableInDatabase();
    	
    	
        
        
        
        
        
    }
    
    public void createTableInDatabase(){
    	Configuration cfr = new Configuration().configure(); 
    	String createType = cfr.getProperty("hibernate.hbm2ddl.auto");
    	if(createType == null || createType.equals("")){
    		createType = "create";
    	}
    	System.out.println(createType);
    	if(createType.equals("create")){
    		SchemaExport export = new SchemaExport(cfr);  
            export.create(true, true);
    	}else if(createType.equals("update")){
    		SchemaUpdate update = new SchemaUpdate(cfr);
            update.execute(true, true);
    	}
    }
}
