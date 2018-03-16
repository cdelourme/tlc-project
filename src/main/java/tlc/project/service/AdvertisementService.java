package tlc.project.service;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import tlc.project.model.Advertisement;

public class AdvertisementService {
	private static AdvertisementService instance = new AdvertisementService();
	public static AdvertisementService getInstance() {
		return instance;
	}
	
	private AdvertisementService() {}
	
	static {
		ObjectifyService.register(Advertisement.class);
	}
	
	
	public List<Advertisement> get(String title, Long priceMin, Long priceMax, Date dateMin, Date dateMax ) {
		
		Query<Advertisement> queryAdvs = ObjectifyService.ofy().load().type(Advertisement.class);
		
		//List<Filter> filters = new ArrayList<>();
		
		if(title != null && !title.isEmpty()) {
			//Equivalent de string contains
			queryAdvs = queryAdvs.filter("title >=", title).filter("title <", title+"\uFFFD");
			//filters.add(new FilterPredicate("title", FilterOperator.GREATER_THAN_OR_EQUAL, title));
			//filters.add(new FilterPredicate("title", FilterOperator.LESS_THAN_OR_EQUAL, title+"\uFFFD"));

		}
		if(priceMin != null) {
			queryAdvs = queryAdvs.filter("price >=", priceMin);
			//filters.add(new FilterPredicate("price", FilterOperator.GREATER_THAN_OR_EQUAL, priceMin));
		}
		if(priceMax != null) {
			queryAdvs = queryAdvs.filter("price <=", priceMax);
			//filters.add(new FilterPredicate("price", FilterOperator.LESS_THAN_OR_EQUAL, priceMax));
		}
		if(dateMin != null) {
			queryAdvs = queryAdvs.filter("date >=", dateMin);
		}
		if(dateMax != null) {
			queryAdvs = queryAdvs.filter("date <=", dateMax);
		}
		//CompositeFilter finalfilter = new CompositeFilter(CompositeFilterOperator.AND, filters);
		//return queryAdvs.filter(finalfilter).list();
		return queryAdvs.list();
	}
	
	public void add(List<Advertisement> advs) {
		ObjectifyService.ofy().save().entities(advs).now();
	}
	
	public void delete(List<Advertisement> advs) {
		ObjectifyService.ofy().delete().entities(advs).now();
	}
}
