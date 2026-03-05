package lab_1

class Car(name: String) : Vehicle(name) {
    override fun start() {
        print("$this.name is driving on the road")
    }

    override fun stop() {
        print("$this.name stopped")
    }

}