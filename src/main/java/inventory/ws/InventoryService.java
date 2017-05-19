package inventory.ws;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import inventory.model.ItemEntity;

@WebService
public class InventoryService {
	private String message = new String("Inventory DAL welcome, ");
    private InventoryDAO dao;
    
    public InventoryService(){
    	dao = new InventoryDaoImpl();
    }
    
    public InventoryService(InventoryDAO idao){
    	this.dao=idao;
    }
    
	@WebMethod
	public String sayHello(@WebParam(name="name")String name) {
	        return message + name + ".";
	}

	
	@WebMethod(operationName="items")
	public Collection<Item> getItems() throws DALException{
		return processList(dao.getItems());
	}
	
	private Collection<Item>  processList(Collection<ItemEntity> l ){
		List<Item> li=new ArrayList<Item>();
		for (ItemEntity ie : l){
			Item i = new Item(ie);
			li.add(i);
		}
		return li;
	}

	@WebMethod(operationName="itemById")
	public Item getItemById(@WebParam(name="id")long id) throws DALException{
		ItemEntity ie =dao.getItemEntityById(id);
		if (ie != null) return new Item(ie);
		return null;
	}
	
	@WebMethod(operationName="itemByName")
	public Item getItemByName(@WebParam(name="name")String name) throws DALException{
		ItemEntity ie =dao.getItemEntityByName(name);
		if (ie != null) return new Item(ie);
		return null;
	}

	@WebMethod(operationName="updateItem")
	public Item updateItem(Item inItem) throws DALException{
		ItemEntity ie = new ItemEntity(inItem);
		return new Item(dao.updateItem(ie));
	}
	
	@WebMethod(operationName="newItem")
	public Item newItem(Item inItem) throws DALException{
		ItemEntity ie = new ItemEntity(inItem);
		ie.setId(null);
		ie.setCreationDate(new Timestamp((new Date()).getTime()));
		ie.setUpdateDate(ie.getCreationDate());		
		if ("Success".equals(dao.createItem(ie))) {
			ItemEntity outItem=dao.getItemEntityByName(ie.getName());
			return new Item(outItem);
		}
		return null;
	}

	@WebMethod(operationName="deleteItem")
	public String deleteItem(@WebParam(name="id")long id) throws DALException{
		return dao.deleteItem(id);
	}

	@WebMethod(operationName="searchByName")
	public Collection<Item> searchByName(String name) throws DALException{
		return processList(dao.searchItemEntitiesByName(name));
	}
}
