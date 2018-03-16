package tlc.project.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

	private List<Advertisement> filterByTitle(List<Advertisement> advs, String title) {
		if(advs != null) {
			if(title != null && !title.isEmpty()) {
				advs = advs.stream().filter(p->p.getTitle().startsWith(title)).collect(Collectors.toList());
			}
		} else {
			if(title != null && !title.isEmpty()) {
				advs = ObjectifyService.ofy().load().type(Advertisement.class)
						.filter("title >=", title).filter("title <", title+"\uFFFD").list();
			}
		}
		return advs;
	}

	private List<Advertisement> filterByPrice(List<Advertisement> advs, Long priceMin, Long priceMax) {
		if(advs != null) {
			if(priceMin != null) {
				advs = advs.stream().filter(p->p.getPrice() >= priceMin).collect(Collectors.toList());
			}
			if(priceMax != null) {
				advs = advs.stream().filter(p->p.getPrice() <= priceMax).collect(Collectors.toList());
			}
		} else {
			Query<Advertisement> queryAdvs = ObjectifyService.ofy().load().type(Advertisement.class);
			if(priceMin != null) {
				queryAdvs = queryAdvs.filter("price >=", priceMin);
			}
			if(priceMax != null) {
				queryAdvs = queryAdvs.filter("price <=", priceMax);
			}
			advs = queryAdvs.list();
		}
		return advs;
	}

	private List<Advertisement> filterByDate(List<Advertisement> advs, Date dateMin, Date dateMax) {
		if(advs != null) {
			if(dateMin != null) {
				advs = advs.stream().filter(p->p.getDate().after(dateMin)).collect(Collectors.toList());
			}
			if(dateMax != null) {
				advs = advs.stream().filter(p->p.getDate().before(dateMax)).collect(Collectors.toList());
			}
		} else {
			Query<Advertisement> queryAdvs = ObjectifyService.ofy().load().type(Advertisement.class);
			if(dateMin != null) {
				queryAdvs = queryAdvs.filter("date >=", dateMin);
			}
			if(dateMax != null) {
				queryAdvs = queryAdvs.filter("date <=", dateMax);
			}
			advs = queryAdvs.list();
		}
		return advs;
	}


	public List<Advertisement> get(String title, Long priceMin, Long priceMax, Date dateMin, Date dateMax ) {
		List<Advertisement> advs = null;
		advs = filterByTitle(advs,title);
		advs = filterByPrice(advs, priceMin, priceMax);
		return filterByDate(advs, dateMin, dateMax);
	}

	public void add(List<Advertisement> advs) {
		ObjectifyService.ofy().save().entities(advs).now();
	}

	public void delete(List<Advertisement> advs) {
		ObjectifyService.ofy().delete().entities(advs).now();
	}
}
