package com.linkedin.backend.configuration;

import com.linkedin.backend.features.authentication.model.User;
import com.linkedin.backend.features.authentication.repository.UserRepository;
import com.linkedin.backend.features.authentication.utils.Encoder;
import com.linkedin.backend.features.feed.model.Post;
import com.linkedin.backend.features.feed.repository.PostRepository;
import com.linkedin.backend.features.networking.model.Connection;
import com.linkedin.backend.features.networking.model.Status;
import com.linkedin.backend.features.networking.repository.ConnectionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class LoadDatabaseConfiguration {
    private static final int NUM_USERS = 100;
    private static final int MIN_POSTS_PER_USER = 1;
    private static final int MAX_POSTS_PER_USER = 10;
    private static final int MIN_CONNECTIONS_PER_USER = 5;
    private static final int MAX_CONNECTIONS_PER_USER = 20;
    private final Encoder encoder;
    private final Random random = new Random();

    public LoadDatabaseConfiguration(Encoder encoder) {
        this.encoder = encoder;
    }

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PostRepository postRepository, ConnectionRepository connectionRepository) {
        return args -> {
            List<User> users = createUsers(userRepository);
            createConnections(connectionRepository, users);
            createPosts(postRepository, users);
        };
    }

    private List<User> createUsers(UserRepository userRepository) {
        List<String> firstNames = Arrays.asList("John", "Jane", "Michael", "Emily", "David", "Sarah", "James", "Emma",
                "William", "Olivia", "Liam", "Ava", "Noah", "Isabella", "Ethan", "Sophia", "Mason", "Mia", "Lucas", "Charlotte",
                "Alexander", "Amelia", "Daniel", "Harper", "Joseph", "Evelyn", "Samuel", "Abigail", "Henry", "Elizabeth",
                "Sebastian", "Sofia", "Jack", "Avery", "Owen", "Ella", "Gabriel", "Madison", "Matthew", "Scarlett",
                "Moussa", "Fatou", "Amadou", "Aisha", "Omar", "Aminata", "Ibrahim", "Mariam", "Abdul", "Zainab",
                "Wei", "Xia", "Ming", "Lin", "Hui", "Yan", "Jie", "Ying", "Feng", "Hong",
                "Mohammed", "Fatima", "Ahmed", "Aisha", "Ali", "Zainab", "Hassan", "Mariam", "Hussein", "Amira");

        List<String> lastNames = Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller",
                "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas",
                "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez",
                "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young", "Allen", "King", "Wright", "Scott",
                "Diop", "Sow", "Fall", "Ndiaye", "Diallo", "Ba", "Sy", "Wade", "Gueye", "Mbaye",
                "Wang", "Li", "Zhang", "Liu", "Chen", "Yang", "Huang", "Zhou", "Wu", "Xu",
                "Al-Sayed", "Khan", "Ahmed", "Hassan", "Ali", "Ibrahim", "Rahman", "Sheikh", "Malik", "Qureshi");

        List<String> companies = Arrays.asList("Google", "Microsoft", "Apple", "Amazon", "Meta", "Netflix", "Tesla",
                "Adobe", "Twitter", "LinkedIn", "Spotify", "Uber", "Airbnb", "Salesforce", "Oracle", "IBM", "Intel",
                "Samsung", "Sony", "Docker", "Zoom", "Slack", "GitHub", "GitLab", "Redis", "MongoDB", "Orange",
                "Thales", "Capgemini", "Botify", "Bwat", "EDF", "Algolia", "Zoho", "Shopopop", "Société Générale", "BnpParibas", "Nexitis");

        List<String> positions = Arrays.asList("Software Engineer", "Data Scientist", "Product Manager",
                "DevOps Engineer", "UI/UX Designer", "Full Stack Developer", "Frontend Developer", "Backend Developer",
                "Machine Learning Engineer", "Cloud Architect", "System Administrator", "Database Administrator",
                "Security Engineer", "QA Engineer", "Technical Lead", "Engineering Manager", "CTO", "VP of Engineering",
                "Solutions Architect", "Technical Project Manager");

        List<String> locations = Arrays.asList(
                "San Francisco, US", "New York, US", "Seattle, US", "Boston, US", "Austin, US",
                "London, UK", "Berlin, DE", "Paris, FR", "Amsterdam, NL", "Stockholm, SE",
                "Tokyo, JP", "Singapore, SG", "Sydney, AU", "Toronto, CA", "Vancouver, CA",
                "Dubai, AE", "Tel Aviv, IL", "Bangalore, IN", "Seoul, KR", "Cape Town, ZA",
                "Mumbai, IN", "Shanghai, CN", "São Paulo, BR", "Mexico City, MX", "Dublin, IE");

        List<String> profilePictures = Arrays.asList(
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330",
                "https://images.unsplash.com/photo-1586297135537-94bc9ba060aa",
                "https://images.unsplash.com/photo-1640951613773-54706e06851d",
                "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde",
                "https://images.unsplash.com/photo-1527980965255-d3b416303d12",
                "https://images.unsplash.com/photo-1438761681033-6461ffad8d80",
                "https://images.unsplash.com/photo-1630208232589-e42b29428b19",
                "https://images.unsplash.com/photo-1619895862022-09114b41f16f"
        );

        List<User> users = new ArrayList<>();
        for (int i = 0; i < NUM_USERS - 3; i++) {
            String firstName = firstNames.get(random.nextInt(firstNames.size()));
            String lastName = lastNames.get(random.nextInt(lastNames.size()));
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + random.nextInt(1000) + "@example.com";
            String position = positions.get(random.nextInt(positions.size()));
            String company = companies.get(random.nextInt(companies.size()));
            String location = locations.get(random.nextInt(locations.size()));
            String profilePicture = i < profilePictures.size() ? profilePictures.get(i) : null;

            users.add(createUser(email, lastName, firstName, lastName, position, company, location, profilePicture));
        }

        users.addAll(List.of(
                createUser("john.doe@example.com", "john", "John", "Doe", "Software Engineer", "Docker Inc",
                        "San Francisco, CA",
                        "https://images.unsplash.com/photo-1633332755192-727a05c4013d"),
                createUser("anne.claire@example.com", "anne", "Anne", "Claire", "HR Manager", "eToro", "Paris, Fr",
                        "https://images.unsplash.com/photo-1494790108377-be9c29b29330"),
                createUser("arnauld.manner@example.com", "arnauld", "Arnauld", "Manner", "Product Manager", "Arc",
                        "Dakar, SN",
                        "https://images.unsplash.com/photo-1640960543409-dbe56ccc30e2")
        ));

        return userRepository.saveAll(users);
    }

    private void createConnections(ConnectionRepository connectionRepository, List<User> users) {
        for (User user : users) {
            int numConnections = random.nextInt(MAX_CONNECTIONS_PER_USER - MIN_CONNECTIONS_PER_USER + 1) + MIN_CONNECTIONS_PER_USER;
            Set<User> userConnections = new HashSet<>();

            while (userConnections.size() < numConnections) {
                User recipient = users.get(random.nextInt(users.size()));
                if (!recipient.equals(user) && !userConnections.contains(recipient)) {
                    userConnections.add(recipient);
                    Connection connection = new Connection(user, recipient);
                    connection.setStatus(Status.ACCEPTED);
                    connectionRepository.save(connection);
                }
            }
        }
    }

    private void createPosts(PostRepository postRepository, List<User> users) {
        List<String> postTemplates = Arrays.asList(
                "Excited to share that %s just launched a new feature!",
                "Great discussion about %s at today's team meeting.",
                "Looking forward to the upcoming %s conference!",
                "Just completed a certification in %s. Always learning!",
                "Proud to announce that our team at %s achieved a major milestone.",
                "Interesting article about the future of %s in tech.",
                "Sharing my thoughts on the latest developments in %s.",
                "Amazing workshop on %s today!",
                "Big announcement: We're hiring %s experts at %s!",
                "Reflecting on my journey as a %s at %s.",
                "Here's what I learned about %s this week.",
                "Exciting times ahead for %s technology!",
                "Just published an article about %s best practices.",
                "Grateful for the amazing %s team at %s.",
                "Innovation in %s is moving faster than ever!"
        );

        List<String> topics = Arrays.asList("AI", "Machine Learning", "Cloud Computing", "DevOps", "Blockchain",
                "Cybersecurity", "Data Science", "IoT", "5G", "Quantum Computing", "AR/VR", "Digital Transformation",
                "Agile Development", "Remote Work", "Tech Leadership");

        List<String> postImages = Arrays.asList(
                "https://images.unsplash.com/photo-1731176497854-f9ea4dd52eb6",
                "https://images.unsplash.com/photo-1504384764586-bb4cdc1707b0",
                "https://images.unsplash.com/photo-1553877522-43269d4ea984",
                "https://images.unsplash.com/photo-1531297484001-80022131f5a1"
        );

        for (User user : users) {
            int numPosts = random.nextInt(MAX_POSTS_PER_USER - MIN_POSTS_PER_USER + 1) + MIN_POSTS_PER_USER;

            for (int i = 0; i < numPosts; i++) {
                String template = postTemplates.get(random.nextInt(postTemplates.size()));
                String topic = topics.get(random.nextInt(topics.size()));
                String content = String.format(template, topic, user.getCompany());

                Post post = new Post(content, user);
                post.setLikes(generateLikes(users, random));

                // Add an image to ~20% of posts
                if (random.nextInt(5) == 0) {
                    post.setPicture(postImages.get(random.nextInt(postImages.size())));
                }

                postRepository.save(post);
            }
        }
    }

    private HashSet<User> generateLikes(List<User> users, Random random) {
        HashSet<User> likes = new HashSet<>();
        int maxLikes = Math.min(50, users.size() / 10); // Maximum 50 likes or 10% of users
        int likesCount = random.nextInt(maxLikes);

        while (likes.size() < likesCount) {
            likes.add(users.get(random.nextInt(users.size())));
        }

        return likes;
    }

    private User createUser(String email, String password, String firstName, String lastName,
                            String position, String company, String location, String profilePicture) {
        User user = new User(email, encoder.encode(password));
        user.setEmailVerified(true);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPosition(position);
        user.setCompany(company);
        user.setLocation(location);
        user.setProfilePicture(profilePicture);
        user.setAbout("I'm a passionate " + position + " at " + company + " with expertise in " +
                generateRandomExpertise() + ". Based in " + location + ".");
        return user;
    }

    private String generateRandomExpertise() {
        List<String> skills = Arrays.asList("cloud architecture", "distributed systems", "machine learning",
                "data analytics", "mobile development", "web development", "DevOps", "cybersecurity",
                "UI/UX design", "artificial intelligence", "blockchain", "IoT");
        return skills.get(random.nextInt(skills.size()));
    }
}
