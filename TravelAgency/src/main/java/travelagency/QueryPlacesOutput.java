package travelagency;

public class QueryPlacesOutput {
        public String placeID;
        public String place;
        public int price;
        public int spots;
        public String vehicle;
        public int visible;

    public QueryPlacesOutput(String placeID,String place, int price, int spots, String vehicle, int visible) {
        this.placeID = placeID;
        this.place = place;
        this.price = price;
        this.spots = spots;
        this.vehicle = vehicle;
        this.visible = visible;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSpots() {
        return spots;
    }

    public void setSpots(int spots) {
        this.spots = spots;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
}
