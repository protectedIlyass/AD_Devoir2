package tp.rest;

import tp.model.*;

import javax.ws.rs.*;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.http.HTTPException;

import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

@Path("/zoo-manager/")
public class MyServiceTP {

	private Center center = new Center(new LinkedList<>(), new Position(49.30494d, 1.2170602d), "Biotropica");

	public MyServiceTP() {
		// Fill our center with some animals
		Cage usa = new Cage("usa", new Position(49.305d, 1.2157357d), 25,
				new LinkedList<>(Arrays.asList(new Animal("Tic", "usa", "Chipmunk", UUID.randomUUID()),
						new Animal("Tac", "usa", "Chipmunk", UUID.randomUUID()))));

		Cage amazon = new Cage("amazon", new Position(49.305142d, 1.2154067d), 15,
				new LinkedList<>(Arrays.asList(new Animal("Canine", "amazon", "Piranha", UUID.randomUUID()),
						new Animal("Incisive", "amazon", "Piranha", UUID.randomUUID()),
						new Animal("Molaire", "amazon", "Piranha", UUID.randomUUID()),
						new Animal("De lait", "amazon", "Piranha", UUID.randomUUID()))));

		center.getCages().addAll(Arrays.asList(usa, amazon));
	}

	/**
	 * GET method bound to calls on /animals/{something}
	 */
	@GET
	@Path("/animals/{id}/")
	@Produces("application/xml")
	public Animal getAnimal(@PathParam("id") String animal_id) throws JAXBException {
		try {
			return center.findAnimalById(UUID.fromString(animal_id));
		} catch (AnimalNotFoundException e) {
			throw new HTTPException(404);
		}
	}

	/**
	 * GET method bound to calls on /animals
	 */
	@GET
	@Path("/animals/")
	@Produces("application/xml")
	public Center getAnimals() {
		return this.center;
	}

	/**
	 * POST method bound to calls on /animals
	 */
	@POST
	@Path("/animals/")
	@Consumes({ "application/xml", "application/json" })
	public Center postAnimals(Animal animal) throws JAXBException {
		this.center.getCages().stream().filter(cage -> cage.getName().equals(animal.getCage())).findFirst()
				.orElseThrow(() -> new HTTPException(404)).getResidents().add(animal);
		return this.center;
	}

	/**
	 * Put method
	 */
	@PUT
	@Path("/animals/")
	@Consumes({ "application/xml", "application/json" })
	public Center putAnimals(Cage c) throws JAXBException {

		this.center.getCages().stream().filter(cage -> cage.getName().equals(c.getName())).findFirst()
				.orElseThrow(() -> new HTTPException(404)).setResidents(c.getResidents());

		return this.center;
	}

	/**
	 * Delete method bound to calls on /animals
	 */
	@DELETE
	@Path("/animals/")
	@Produces("application/xml")

	public Center deleteAnimals() throws JAXBException {
		this.center.getCages().clear();

		Cage usa = new Cage("usa", new Position(49.305d, 1.2157357d), 25, null);
		Cage amazon = new Cage("amazon", new Position(49.305142d, 1.2154067d), 15, null);
		this.center.getCages().addAll(Arrays.asList(usa, amazon));
		return this.center;
	}

	/**
	 * Delete method bound to calls on /animals
	 */
	@DELETE
	@Path("/animals/byCage/{cage}")
	@Produces("application/xml")

	public Center deleteAnimals(@RequestParam String cageParam) throws JAXBException {
		Cage c = this.center.getCages().stream().filter(cage -> cage.getName().equals(cageParam)).findFirst()
				.orElseThrow(() -> new HTTPException(404));
		c.getResidents().clear();

		return this.center;
	}

	/**
	 * POST method bound to calls on /cages
	 */
	@POST
	@Path("/cages/")
	@Consumes({ "application/xml", "application/json" })
	@Produces("application/xml")
	public Center PostCages(Cage cage) {
		this.center.add_cage(cage);
		return this.center;
	}

	/**
	 * find animal by its name
	 * 
	 * @throws AnimalNotFoundException
	 */
	@GET
	@Path("/find/byName/{name}/")
	@Produces("application/xml")
	public Animal findByName(@PathParam("name") String animal_name) throws JAXBException, AnimalNotFoundException {

		return center.findAnimalById(center.findAnimalByName(animal_name).getId());
	}

	/**
	 * find animals at given position
	 */
	@GET
	@Path("/find/at/{position}/")
	@Produces("application/xml")
	public Cage findAt(@PathParam("position") String position) throws JAXBException {

		String[] coord = position.split(",");
		Position p = new Position();
		p.setLatitude(Double.parseDouble(coord[0]));
		p.setLongitude(Double.parseDouble(coord[1]));
		return this.center.getCages().stream().filter(cage -> cage.getPosition().equals(p)).findFirst()
				.orElseThrow(() -> new HTTPException(404));

	}

	/**
	 * find animals near given position
	 */
	@GET
	@Path("/find/near/{position}/")
	@Produces("application/xml")
	public Cage findNear(@PathParam("position") String position) throws JAXBException {

		String[] coord = position.split(",");
		Position p = new Position();
		p.setLatitude(Double.parseDouble(coord[0]));
		p.setLongitude(Double.parseDouble(coord[1]));
		return this.center.getCages().stream().filter(cage -> cage.getPosition().near(p)).findFirst()
				.orElseThrow(() -> new HTTPException(404));

	}

	/**
	 * add an animal with given id
	 */
	@POST
	@Path("/animals/{id}")
	@Consumes({ "application/xml", "application/json" })
	@Produces("application/xml")
	public Center postAnimal(Animal animal, @PathParam("id") String animal_id) throws JAXBException {

		try {
			return center.createAnimalById(animal, UUID.fromString(animal_id));
		} catch (AnimalNotFoundException e) {
			throw new HTTPException(404);
		}
	}

	/**
	 * delete animal with id
	 */
	@DELETE
	@Path("/animals/{id}")
	@Produces("application/xml")
	public Center deleteAnimal(@PathParam("id") String animal_id) {

		try {
			return center.deleteAnimalById(UUID.fromString(animal_id));
		} catch (AnimalNotFoundException e) {
			throw new HTTPException(404);
		}
	}

	/**
	 * update animal
	 * 
	 * @throws AnimalNotFoundException
	 */
	@PUT
	@Path("/animals/{id}")
	@Consumes({ "application/xml", "application/json" })
	@Produces("application/xml")
	public Center update_animal(Animal new_animal, @PathParam("id") String animal_id) throws AnimalNotFoundException {

		Animal old_animal = center.findAnimalById(UUID.fromString(animal_id));
		// supprimer l'ancien animal
		this.center.getCages().stream().filter(cage -> cage.getName().equals(old_animal.getCage())).findFirst()
				.orElseThrow(() -> new HTTPException(404)).getResidents().remove(old_animal);
		// ajouter le nouvel animal
		this.center
				.addAnimal(this.center.getCages().stream().filter(cage -> cage.getName().equals(new_animal.getCage()))
						.findFirst().orElseThrow(() -> new HTTPException(404)), new_animal);

		return this.center;
	}

	@GET
	@Path("animals/{id}/wolf/")
	@Produces("application/xml")
	public Source wolframInfo(@PathParam("id") String animal_id) throws JAXBException {
		try {
			Animal animal = this.center.findAnimalById(UUID.fromString(animal_id));
			InputStreamReader stream = new InputStreamReader(new URL(
					"http://api.wolframalpha.com/v2/query?input=" + animal.getSpecies() + "&appid=2ERYU8-K893TWXRQX")
							.openStream());

			return new StreamSource(stream);
		} catch (MalformedURLException e) {
			throw new HTTPException(404);
		} catch (IOException e) {
			throw new HTTPException(404);
		} catch (AnimalNotFoundException e) {
			throw new HTTPException(404);
		}
	}

}
