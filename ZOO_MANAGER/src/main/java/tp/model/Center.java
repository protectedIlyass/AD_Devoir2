package tp.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.http.HTTPException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

@XmlRootElement
public class Center {

    Collection<Cage> cages;
    Position position;
    String name;

    public Center() {
        cages = new LinkedList<>();
    }

    public Center(Collection<Cage> cages, Position position, String name) {
        this.cages = cages;
        this.position = position;
        this.name = name;
    }
    
public void addAnimal(Cage cage, Animal animal ){
    	
    	if (cage.getResidents()==null){
    		
    		cage.setResidents(new LinkedList<>(Arrays.asList(animal)));
    	}
    	
    if (cage.getCapacity()>cage.getResidents().size()){
    	
    	cage.getResidents().add(animal);}
    else
    {
    	System.out.println("\n La Cage est pleine ! Impossible d'ajouter l'animal "+animal.getName()+ "\n");
    	cage.setFull();
    	}
}
    
    public Animal findAnimalByName(String name) throws AnimalNotFoundException {
        return this.cages.stream()
                .map(Cage::getResidents)
                .flatMap(Collection::stream)
                .filter(animal -> name.equals(animal.getName()))
                .findFirst()
                .orElseThrow(AnimalNotFoundException::new);
    }


    public Center createAnimalById(Animal animal,UUID uuid) throws AnimalNotFoundException {
    	animal.setId(uuid);
    	
    	this.getCages()
        .stream()
        .filter(cage -> cage.getName().equals(animal.getCage()))
        .findFirst()
        .orElseThrow(() -> new HTTPException(404))
        .getResidents()
        .add(animal);
    	
    	return this;
    }
       
    
    public Animal findAnimalById(UUID uuid) throws AnimalNotFoundException {
        return this.cages.stream()
                .map(Cage::getResidents)
                .flatMap(Collection::stream)
                .filter(animal -> uuid.equals(animal.getId()))
                .findFirst()
                .orElseThrow(AnimalNotFoundException::new);
    }
    
    public Center deleteAnimalById(UUID uuid) throws AnimalNotFoundException {

    	for(Cage cage : this.cages){
    		for(Animal a : cage.getResidents()){
    			if(uuid.equals(a.getId()))
    				cage.getResidents().remove(a);
    		}
    	}
        
         return this;
    }
    
    public void add_cage(Cage cage){
    	this.cages.add(cage);
    }

    public Collection<Cage> getCages() {
        return cages;
    }

    public Position getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public void setCages(Collection<Cage> cages) {
        this.cages = cages;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }
}
