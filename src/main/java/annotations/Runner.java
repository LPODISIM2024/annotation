package annotations;

public class Runner {

	public static void main(String[] args) {
		ObjectConverter o2jc = new ObjectConverter();
		
		Person pa = new Person();
		pa.setFirstName("juri");
		pa.setLastName("di rocco");
		pa.setAge("5");

		try {
			o2jc.apply(pa);
			System.out.println(pa.getFirstName());
			System.out.println(pa.getLastName());
		} catch (JsonSerializationException e) {
			System.err.println("ERRORE");
			System.err.println(pa.getFirstName());
			System.err.println(pa.getLastName());
		}

		PersonNoAnnotaion pna = new PersonNoAnnotaion();
		pna.setFirstName("juri");
		pna.setLastName("di Rocco");
		
		
		try {
			pna.setAge("5");
			o2jc.apply(pna);
			System.out.println("OK");
			System.out.println(pna.getFirstName());
			System.out.println(pna.getLastName());

		} catch (JsonSerializationException e) {
			System.err.println("ERRORE pna");
			System.err.println(pna.getFirstName());
			System.err.println(pna.getLastName());
		}

	}

}
