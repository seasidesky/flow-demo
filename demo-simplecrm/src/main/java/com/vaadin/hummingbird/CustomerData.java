package com.vaadin.hummingbird;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.vaadin.teemu.jsoncontainer.JsonContainer;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;

public class CustomerData {
	
	private JsonContainer dataSource = null;
	
	public CustomerData() {
		InputStream is = this.getClass().getResourceAsStream("customers-snapshot.json");
		try {
			String json = IOUtils.toString(is, "UTF-8");
			dataSource = JsonContainer.Factory.newInstance(json);
			is.close();
		} catch (Exception e) {
			System.out.println("Creating DataSource exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void deleteCustomer(Object itemId) {
		dataSource.removeItem(itemId);
	}
	
	public void filterCustomers(String filterText) {
		dataSource.removeAllContainerFilters();
		if (filterText == null || ("".equals(filterText))) {
			return;
		}
		Filter filter1 = new SimpleStringFilter("lastName", filterText, true, false);
		Filter filter2 = new SimpleStringFilter("firstName", filterText, true, false);
		Filter filter3 = new SimpleStringFilter("email", filterText, true, false);
		Filter filter4 = new SimpleStringFilter("status", filterText, true, true);
		Or orFilters = new Or(filter1,filter2,filter3,filter4);
		dataSource.addContainerFilter(orFilters);

	}
	
	public HashMap<String, Integer> getStatusCounts() {
		return getKeyCounts("status");
	}

	private List<HashMap<String, Object>> getAgeCategories() {
		List<HashMap<String, Object>> categories = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> category = new HashMap<String, Object>();
		category.put("start", 0);
		category.put("end", 14);
		category.put("label", "0-15");
		categories.add(category);
		
		category = new HashMap<String, Object>();
		category.put("start", 15);
		category.put("end", 29);
		category.put("label", "15-30");
		categories.add(category);
		
		category = new HashMap<String, Object>();
		category.put("start", 30);
		category.put("end", 59);
		category.put("label", "30-60");
		categories.add(category);
		
		category = new HashMap<String, Object>();
		category.put("start", 60);
		category.put("end", 100);
		category.put("label", "60-100");
		categories.add(category);
	
		return categories;
	}
	
	public HashMap<String, Integer> getAgesCounts() {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		List<HashMap<String, Object>> categories = getAgeCategories();
		Collection ids = getDataSource().getItemIds();
		Iterator iter = ids.iterator();
		while (iter.hasNext()) {
			Object id = iter.next();
			String birthDate = (String) getDataSource().getItem(id).getItemProperty("birthDate").getValue();
			LocalDate fromString = LocalDate.parse( birthDate );
			Period period = Period.between(fromString, LocalDate.now());
			for (Iterator<HashMap<String,Object>> categoryIter = categories.iterator(); categoryIter.hasNext();) {
				HashMap<String, Object> category = categoryIter.next();
				Integer start = (Integer) category.get("start");
				Integer end = (Integer) category.get("end");
				String label = (String) category.get("label");
				if (period.getYears() >= start && period.getYears() <= end) {
					if (!result.containsKey(label)) {
						result.put(label, 1);
					} else {
						result.put(label, (result.get(label) + 1));
					}		
				}
			}
			
			
		}
		return result;
	}
	
	private HashMap<String, Integer> getKeyCounts(String key) {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		Collection ids = getDataSource().getItemIds();
		Iterator iter = ids.iterator();
		while (iter.hasNext()) {
			Object id = iter.next();
			String value = (String) getDataSource().getItem(id).getItemProperty(key).getValue();
			if (!result.containsKey(value)) {
				result.put(value, 1);
			} else {
				result.put(value, (result.get(value) + 1));
			}
			
		}
		return result;
	}
	
	public HashMap<String, Integer> getGenderCounts() {
		return getKeyCounts("gender");
	} 
	
	public Container.Indexed getDataSource() {
		return dataSource;
	}
	
	public Item getItem(Object itemId) {
		return dataSource.getItem(itemId);
	}
	
	public void modelFromItemId(Object itemId, CustomerModel cm) {
		Item item = dataSource.getItem(itemId);
		mapItemToModel(item, cm);
	}
	
	protected void updateCustomer(Object itemId, CustomerModel customer) {
		Item item = dataSource.getItem(itemId);
		mapModelToItem(customer, item);
	}
	
	private void mapItemToModel(Item item, CustomerModel cm) {
		cm.setEmail((String) item.getItemProperty("email").getValue());
		cm.setFirstName((String) item.getItemProperty("firstName").getValue());
		cm.setLastName((String) item.getItemProperty("lastName").getValue());
		cm.setBirthDate((String) item.getItemProperty("birthDate").getValue());
		cm.setGender((String) item.getItemProperty("gender").getValue());
		cm.setStatus((String) item.getItemProperty("status").getValue());
	}
	
	private void mapModelToItem(CustomerModel cm, Item item) {
		item.getItemProperty("email").setValue(cm.getEmail());
		item.getItemProperty("firstName").setValue(cm.getFirstName());
		item.getItemProperty("lastName").setValue(cm.getLastName());
		item.getItemProperty("birthDate").setValue(cm.getBirthDate());	
		item.getItemProperty("gender").setValue(cm.getGender());
		item.getItemProperty("status").setValue(cm.getStatus());
	}

}
