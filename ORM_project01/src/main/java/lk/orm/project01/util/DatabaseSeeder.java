package lk.orm.project01.util;

import lk.orm.project01.bo.BOFactory;
import lk.orm.project01.bo.RegisterBO;
import lk.orm.project01.bo.TherapyProgrammeBO;
import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.dto.TherapyProgrammeDTO;


public class DatabaseSeeder {

    private static final RegisterBO       registerBO =
        (RegisterBO)       BOFactory.getInstance().getBO(BOFactory.BOTypes.REGISTER);
    private static final TherapyProgrammeBO programmeBO =
        (TherapyProgrammeBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.THERAPY_PROGRAMME);


    private DatabaseSeeder() {}


    public static void seed() {
        seedAdminUser();
        seedTherapyProgrammes();
    }



    private static void seedAdminUser() {
        try {
            registerBO.register(new RegisterDTO(
                "admin",
                "Admin@1234",   // Will be BCrypt-hashed by RegisterBOImpl
                "ADMIN",
                "admin@serenity.lk"
            ));
            System.out.println("[Seeder] Default admin user created. Username: admin | Password: Admin@1234");
        } catch (Exception e) {
            // User already exists — skip silently
        }
    }

    // ---- Seed Therapy Programmes ----

    private static void seedTherapyProgrammes() {
        TherapyProgrammeDTO[] programmes = {
            new TherapyProgrammeDTO(
                "MT1001",
                "Cognitive Behavioral Therapy",
                "A structured, goal-oriented psychotherapy that challenges negative thought patterns " +
                "and behaviors to improve emotional regulation and develop personal coping strategies.",
                "12 weeks",
                80000.0
            ),
            new TherapyProgrammeDTO(
                "MT1002",
                "Mindfulness-Based Stress Reduction",
                "An evidence-based program that uses mindfulness meditation and yoga to reduce stress, " +
                "anxiety, depression, and chronic pain.",
                "8 weeks",
                50000.0
            ),
            new TherapyProgrammeDTO(
                "MT1003",
                "Dialectical Behavior Therapy",
                "A comprehensive cognitive-behavioral treatment for complex, difficult-to-treat mental " +
                "disorders. Emphasizes balancing acceptance and change.",
                "16 weeks",
                100000.0
            ),
            new TherapyProgrammeDTO(
                "MT1004",
                "Group Therapy Sessions",
                "Therapeutic sessions conducted in a group setting, allowing participants to share " +
                "experiences, gain support, and develop social skills under professional guidance.",
                "6 months",
                120000.0
            ),
            new TherapyProgrammeDTO(
                "MT1005",
                "Family Counseling",
                "Therapy that works with families to nurture change and development. Helps family " +
                "members improve communication and resolve conflicts.",
                "3 months",
                40000.0
            )
        };

        for (TherapyProgrammeDTO dto : programmes) {
            try {
                programmeBO.saveProgramme(dto);
                System.out.println("[Seeder] Programme seeded: " + dto.getProgrammeId() + " - " + dto.getName());
            } catch (Exception e) {
                // Programme already exists — skip silently
            }
        }
    }
}
