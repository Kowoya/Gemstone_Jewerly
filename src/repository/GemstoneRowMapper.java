package repository;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GemstoneRowMapper {

    public Gemstone map(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        String name = rs.getString("name");
        double weight = rs.getDouble("weight_carats");
        double price = rs.getDouble("price_per_carat");
        ClarityGrade clarity = ClarityGrade.valueOf(rs.getString("clarity"));
        double transparency = rs.getDouble("transparency_index");
        GemColor color = GemColor.valueOf(rs.getString("color"));
        Origin origin = Origin.valueOf(rs.getString("origin"));

        String certificate = rs.getString("certificate_number");
        String cutText = rs.getString("cut_type");
        CutType cut = cutText == null ? null : CutType.valueOf(cutText);
        String treatment = rs.getString("treatment_type");

        Gemstone gemstone = switch (type) {
            case "DIAMOND" -> new Diamond(name, weight, price, clarity,
                    transparency, color, origin, certificate, cut);
            case "RUBY" -> new Ruby(name, weight, price, clarity,
                    transparency, color, origin, certificate, cut);
            case "SAPPHIRE" -> new Sapphire(name, weight, price, clarity,
                    transparency, color, origin, certificate, cut);
            case "EMERALD" -> new Emerald(name, weight, price, clarity,
                    transparency, color, origin, certificate, cut);
            case "AMETHYST" -> new Amethyst(name, weight, price, clarity,
                    transparency, color, origin, treatment);
            case "TOPAZ" -> new Topaz(name, weight, price, clarity,
                    transparency, color, origin, treatment);
            case "GARNET" -> new Garnet(name, weight, price, clarity,
                    transparency, color, origin, treatment);
            default -> throw new DataAccessException("Unknown type: " + type);
        };
        gemstone.setId(rs.getInt("id"));
        return gemstone;
    }
}