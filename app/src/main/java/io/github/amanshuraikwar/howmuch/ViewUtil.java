package io.github.amanshuraikwar.howmuch;

public class ViewUtil {

    public static int getCategoryColor(String categoryTitle) {
        switch (categoryTitle.toLowerCase()) {
            case "food": return R.color.food;
            case "health/medical": return R.color.health;
            case "home": return R.color.home;
            case "transportation": return R.color.transportation;
            case "personal": return R.color.personal;
            case "utilities": return R.color.utilities;
            case "travel": return R.color.travel;
            default: return R.color.activeIcon;
        }
    }

    public static int getCategoryIcon(String categoryTitle) {
        switch (categoryTitle.toLowerCase()) {
            case "food": return R.drawable.round_fastfood_24;
            case "health/medical": return R.drawable.round_healing_24;
            case "home": return R.drawable.round_home_24;
            case "transportation": return R.drawable.round_commute_24;
            case "personal": return R.drawable.round_person_24;
            case "utilities": return R.drawable.round_shopping_cart_24;
            case "travel": return R.drawable.round_flight_takeoff_24;
            default: return R.drawable.round_bubble_chart_24;
        }
    }
}
