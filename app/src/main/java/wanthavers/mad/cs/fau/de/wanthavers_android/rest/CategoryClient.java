package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.rest.api.CategoryResource;

public class CategoryClient extends RestClient {
    private static CategoryClient INSTANCE;

    private CategoryResource categoryEndpoint;

    private CategoryClient(Context context) {
        super(context);
    }

    @Override
    protected void buildNewEndpoint() {
        categoryEndpoint = null;
        categoryEndpoint = WebResourceFactory.newResource(CategoryResource.class, target);
    }

    public static CategoryClient getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CategoryClient(context);
        }
        return INSTANCE;
    }

    public Category getCategory(long categoryId) {
        return categoryEndpoint.get(null, categoryId);
    }

    public List<Category> getSubcategories(long categoryId, boolean recursive) {
        return categoryEndpoint.getSub(null, categoryId, recursive);
    }

    public List<Desire> getAllDesiresForCategory(long categoryId, boolean recursive) {
        return  categoryEndpoint.getDesires(categoryId, recursive);
    }
}
