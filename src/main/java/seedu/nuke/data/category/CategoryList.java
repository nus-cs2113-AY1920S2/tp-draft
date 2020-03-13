package seedu.nuke.data.category;

import seedu.nuke.data.module.Module;
import seedu.nuke.data.module.ModuleList;
import seedu.nuke.data.task.TaskList;
import seedu.nuke.exception.DataNotFoundException;
import seedu.nuke.exception.DuplicateDataException;

import java.util.ArrayList;

public class CategoryList {
    private ArrayList<Category> categoryList;

    public CategoryList(Module parentModule) {
        categoryList = initialiseCategories(parentModule);
    }

    public ArrayList<Category> initialiseCategories(Module parentModule) {
        ArrayList<Category> initialTaskCategories = new ArrayList<>();
        initialTaskCategories.add(new Category(parentModule, "Lecture", 1));
        initialTaskCategories.add(new Category(parentModule, "Tutorial", 2));
        initialTaskCategories.add(new Category(parentModule, "Assignment", 4));
        initialTaskCategories.add(new Category(parentModule, "Lab", 3));

        return initialTaskCategories;
    }

    public Category getCategory(String categoryName) throws CategoryNotFoundException {
        for (Category category : categoryList) {
            if (category.getCategoryName().equals(categoryName)) {
                return category;
            }
        }
        throw new CategoryNotFoundException();
    }


    /**
     * Checks for duplicates of the specified category in the Category List.
     * @param toCheck   The category to check
     * @return <code>TRUE</code> if there exists a duplicate, and <code>FALSE</code> otherwise
     */
    private boolean contains(Category toCheck) {
        for (Category category : categoryList) {
            if (category.isSameCategory(toCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a category to the category List.
     *
     * @param toAdd the category to add
     */
    public void add(Category toAdd) throws DuplicateCategoryException {
        if (contains(toAdd)) {
            throw new DuplicateCategoryException();
        } else {
            categoryList.add(toAdd);
        }
    }

    public void delete(Category toDelete) {
        categoryList.remove(toDelete);
    }


    /**
     * Deletes a <b>Category</b> with the specified <code>category name</code> from the <b>Category List</b>.
     *
     * @param categoryName The category name of the <b>Category</b> to be deleted
     * @throws CategoryNotFoundException  If the category with the specified category name is not found in the <b>Category List</b>
     * @see Category
     */
    public Category delete(String categoryName) throws CategoryNotFoundException {
        Category toDelete = getCategory(categoryName);
        categoryList.remove(toDelete);
        return toDelete;
    }

    public TaskList filterExact(String categoryName) throws CategoryNotFoundException {
        return getCategory(categoryName).getTasks();
    }

    public static class CategoryNotFoundException extends DataNotFoundException {}
    public static class DuplicateCategoryException extends DuplicateDataException {}
}
