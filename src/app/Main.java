package app;

import database.DatabaseConfig;
import enums.CutType;
import model.Gemstone;
import model.Necklace;
import enums.ClarityGrade;
import enums.GemColor;
import enums.Origin;
import model.Emerald;
import repository.GemstoneRepository;
import repository.GemstoneRowMapper;
import repository.NecklaceRepository;
import service.NecklaceBuilderService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        DatabaseConfig config = DatabaseConfig.load("db.properties");
        GemstoneRepository repository =
                new GemstoneRepository(config, new GemstoneRowMapper());

        Emerald newStone = new Emerald("Зелений вогонь", 2.1, 2400, ClarityGrade.VS2,
                7.2, GemColor.GREEN, Origin.ZAMBIA, "GRS-5512001", CutType.EMERALD);
        repository.save(newStone);
        System.out.println("Новий камінь отримав id " + newStone.getId());

        List<Gemstone> catalog = repository.findAll();
        System.out.println("Каталог з бази (" + catalog.size() + " каменів):");
        for (Gemstone gemstone : catalog) {
            System.out.println("  " + gemstone.getId() + " " + gemstone.getName()
                    + " - " + gemstone.calculateValue());
        }

        NecklaceBuilderService builder = new NecklaceBuilderService();
        Necklace necklace = builder.buildNecklace(1, "Вечірнє намисто",
                20000, catalog, 5);

        System.out.println("Загальна вага: "
                + necklace.getTotalWeightCarats() + " ct");
        System.out.println("Загальна вартість: " + necklace.getTotalValue());

        necklace.sortByValue();
        System.out.println("Камені за цінністю:");
        for (Gemstone gemstone : necklace.getGemstones()) {
            System.out.println("  " + gemstone.getName() + " - "
                    + gemstone.calculateValue());
        }

        List<Gemstone> found = necklace.sortByClarity(9.0, 10.0);
        System.out.println("Прозорість від 9 до 10:");
        for (Gemstone gemstone : found) {
            System.out.println("  " + gemstone.getName());
        }

        NecklaceRepository necklaceRepository =
                new NecklaceRepository(config, new GemstoneRowMapper());
        necklaceRepository.save(necklace);
        System.out.println("Намисто збережено в базу");

        Necklace loaded = necklaceRepository.findById(1).orElseThrow();
        System.out.println("Завантажено з бази: " + loaded.getName()
                + ", каменів: " + loaded.getGemstoneCount()
                + ", вартість: " + loaded.getTotalValue());

        Necklace dayNecklace = new Necklace(2, "Денне намисто");
        dayNecklace.addGemstone(catalog.get(7));   // Замбійський
        dayNecklace.addGemstone(catalog.get(8));   // Імперський
        dayNecklace.addGemstone(catalog.get(10));  // Демантоїд
        necklaceRepository.save(dayNecklace);

        System.out.println("Нове намисто збережено: " + dayNecklace.getName());
        for (Gemstone gemstone : dayNecklace.getGemstones()) {
            System.out.println("  " + gemstone.getName() + " - " + gemstone.calculateValue());
        }
        System.out.println("Вартість: " + dayNecklace.getTotalValue());
    }
}