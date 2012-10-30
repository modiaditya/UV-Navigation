package me.navigation.shared;

public class LatLong {
	
		private Double latitude;
		private Double longitude;

		public LatLong() {
		}

		public LatLong(Double latitude, Double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}

		public String toString()
		{
			return latitude+","+longitude;
		}
		public Double getLatitude() {
			return this.latitude;
		}

		public Double getLongitude() {
			return this.longitude;
		}

		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}

		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		}
}
