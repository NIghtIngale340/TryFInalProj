package com.proj.quiz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;
import com.proj.core.TechXplorerGame;

public class QuizManager implements Disposable {
    // UI Components
    private Stage stage;
    private Skin skin;
    private Table mainTable;
    private Label questionLabel;
    private TextButton[] answerButtons;
    private Label resultLabel;
    private TextButton nextButton;

    // Quiz data
    private Array<Question> questions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private boolean quizCompleted = false;
    private boolean quizPassed = false;

    // Boss info
    private int bossIndex;
    private final int QUESTIONS_PER_BOSS = 5;
    private final int REQUIRED_CORRECT = 3; // Min correct answers to defeat boss

    public QuizManager(int bossIndex) {
        this.bossIndex = bossIndex;

        // Initialize stage
        stage = new Stage(new FitViewport(TechXplorerGame.WORLD_WIDTH, TechXplorerGame.WORLD_HEIGHT));

        // Load skin for UI
        skin = new Skin(Gdx.files.internal("ui/ui_skin.json"));

        // Create questions based on boss index
        createQuestions();

        // Initialize UI
        setupUI();
    }

    private void createQuestions() {
        questions = new Array<>();

        // Create questions based on boss index
        switch (bossIndex) {
            case 0: // SteelWard - PC Case questions
                questions.add(new Question("What is the main purpose of a computer case?",
                    new String[]{"To protect internal components", "To make the computer faster", "To improve graphics", "To store files"},
                    0));
                questions.add(new Question("Which of these is NOT typically found on a PC case front panel?",
                    new String[]{"CPU socket", "USB ports", "Power button", "Audio jacks"},
                    0));
                questions.add(new Question("What does ATX stand for in ATX case?",
                    new String[]{"Advanced Technology Extended", "Automatic Thermal Xchange", "Additional Transistor Xtension", "Audio Transfer Xport"},
                    0));
                questions.add(new Question("Which type of PC case is typically the smallest?",
                    new String[]{"Mini-ITX", "Mid-Tower", "Full-Tower", "Super-Tower"},
                    0));
                questions.add(new Question("What feature helps improve airflow in a PC case?",
                    new String[]{"Mesh front panel", "Solid front panel", "No side panel", "Heavy weight"},
                    0));
                break;

            case 1: // BlazeCinder - Cooling questions
                questions.add(new Question("What is the main purpose of a CPU cooler?",
                    new String[]{"To remove heat from the CPU", "To make the computer quieter", "To add RGB lighting", "To speed up processing"},
                    0));
                questions.add(new Question("Which cooling method typically achieves lower CPU temperatures?",
                    new String[]{"Liquid cooling", "Air cooling", "Passive cooling", "No cooling"},
                    0));
                questions.add(new Question("What is the purpose of thermal paste?",
                    new String[]{"To fill microscopic gaps between CPU and heatsink", "To glue components together", "To paint the motherboard", "To clean dust"},
                    0));
                questions.add(new Question("Which direction should case fans typically be mounted to ensure proper airflow?",
                    new String[]{"Front: intake, Rear: exhaust", "Front: exhaust, Rear: intake", "All intake", "All exhaust"},
                    0));
                questions.add(new Question("What happens if a computer overheats?",
                    new String[]{"It may shut down or throttle performance", "It will run faster", "Battery life will improve", "Storage capacity increases"},
                    0));
                break;

            case 2: // Memorix - Storage questions
                questions.add(new Question("Which storage device typically has the fastest read/write speeds?",
                    new String[]{"NVMe SSD", "SATA SSD", "HDD", "External USB Drive"},
                    0));
                questions.add(new Question("What does SSD stand for?",
                    new String[]{"Solid State Drive", "Super Speed Drive", "Static Storage Device", "System Storage Data"},
                    0));
                questions.add(new Question("What is the main advantage of an HDD over an SSD?",
                    new String[]{"Lower cost per gigabyte", "Faster speed", "More durability", "Less power consumption"},
                    0));
                questions.add(new Question("What does RAM stand for?",
                    new String[]{"Random Access Memory", "Rapid Access Mode", "Read Access Module", "Remote Access Memory"},
                    0));
                questions.add(new Question("Which type of memory is volatile and loses its data when power is turned off?",
                    new String[]{"RAM", "SSD", "HDD", "USB Flash Drive"},
                    0));
                break;

            case 3: // Glitchron - PSU and motherboard questions
                questions.add(new Question("What does PSU stand for in computing?",
                    new String[]{"Power Supply Unit", "Processing System Utility", "Primary Storage Unit", "Peripheral Support Utility"},
                    0));
                questions.add(new Question("What is the main function of a motherboard?",
                    new String[]{"To connect and allow communication between components", "To store data", "To process calculations", "To cool the system"},
                    0));
                questions.add(new Question("What does the 80 Plus certification on a PSU indicate?",
                    new String[]{"Energy efficiency", "Maximum wattage", "Manufacturing quality", "Cable length"},
                    0));
                questions.add(new Question("Which of these is NOT a common motherboard form factor?",
                    new String[]{"XLT", "ATX", "Micro-ATX", "Mini-ITX"},
                    0));
                questions.add(new Question("What component on the motherboard stores the BIOS?",
                    new String[]{"ROM chip", "RAM", "CPU", "GPU"},
                    0));
                break;

            case 4: // EXODUS - Final boss comprehensive questions
                questions.add(new Question("Which technology uses artificial neural networks to mimic human learning?",
                    new String[]{"Deep Learning", "Overclocking", "Defragmentation", "Virtualization"},
                    0));
                questions.add(new Question("What is responsible for rendering graphics in a computer?",
                    new String[]{"GPU", "CPU", "RAM", "PSU"},
                    0));
                questions.add(new Question("What is the brain of the computer?",
                    new String[]{"CPU", "RAM", "Motherboard", "Hard Drive"},
                    0));
                questions.add(new Question("What does IoT stand for?",
                    new String[]{"Internet of Things", "Input/Output Technology", "Internal Operating Transistor", "Integrated Output Terminal"},
                    0));
                questions.add(new Question("What is the purpose of an operating system?",
                    new String[]{"To manage hardware resources and provide services for applications",
                        "To connect to the internet",
                        "To create documents and spreadsheets",
                        "To protect against viruses"},
                    0));
                break;
        }
    }

    private void setupUI() {
        // Create main table for UI layout
        mainTable = new Table();
        mainTable.setFillParent(true);

        // Create question label
        questionLabel = new Label("", skin);
        questionLabel.setWrap(true);

        // Create answer buttons
        answerButtons = new TextButton[4];
        for (int i = 0; i < 4; i++) {
            final int index = i;
            answerButtons[i] = new TextButton("", skin);
            answerButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    checkAnswer(index);
                }
            });
        }

        // Create result label and next button
        resultLabel = new Label("", skin);
        nextButton = new TextButton("Next Question", skin);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextQuestion();
            }
        });

        // Add UI elements to table
        mainTable.add(questionLabel).width(600).pad(20).row();
        for (TextButton button : answerButtons) {
            mainTable.add(button).width(500).pad(10).row();
        }
        mainTable.add(resultLabel).pad(20).row();
        mainTable.add(nextButton).width(200).pad(10).row();

        // Hide result and next button initially
        resultLabel.setVisible(false);
        nextButton.setVisible(false);

        // Add table to stage
        stage.addActor(mainTable);

        // Set input processor
        Gdx.input.setInputProcessor(stage);
    }

    public void startQuiz() {
        // Reset quiz state
        currentQuestionIndex = 0;
        correctAnswers = 0;
        quizCompleted = false;
        quizPassed = false;

        // Show first question
        showQuestion(currentQuestionIndex);
    }

    private void showQuestion(int index) {
        if (index < questions.size) {
            Question question = questions.get(index);
            questionLabel.setText(question.getQuestion());

            String[] answers = question.getAnswers();
            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i].setText(answers[i]);
                answerButtons[i].setDisabled(false);
            }

            // Hide result and next button
            resultLabel.setVisible(false);
            nextButton.setVisible(false);
        } else {
            // Quiz completed
            finishQuiz();
        }
    }

    private void checkAnswer(int selectedAnswer) {
        Question currentQuestion = questions.get(currentQuestionIndex);
        boolean correct = (selectedAnswer == currentQuestion.getCorrectAnswerIndex());

        if (correct) {
            correctAnswers++;
            resultLabel.setText("Correct!");
        } else {
            resultLabel.setText("Wrong! The correct answer is: " +
                currentQuestion.getAnswers()[currentQuestion.getCorrectAnswerIndex()]);
        }

        // Disable answer buttons
        for (TextButton button : answerButtons) {
            button.setDisabled(true);
        }

        // Show result and next button
        resultLabel.setVisible(true);
        nextButton.setVisible(true);
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        showQuestion(currentQuestionIndex);
    }

    private void finishQuiz() {
        // Determine if quiz is passed
        quizPassed = (correctAnswers >= REQUIRED_CORRECT);
        quizCompleted = true;

        // Show final result
        questionLabel.setText("Quiz Completed!");

        // Hide answer buttons
        for (TextButton button : answerButtons) {
            button.setVisible(false);
        }

        // Show result
        if (quizPassed) {
            resultLabel.setText("You defeated the boss!\nCorrect answers: " + correctAnswers + "/" + questions.size);
        } else {
            resultLabel.setText("You failed to defeat the boss.\nCorrect answers: " + correctAnswers + "/" + questions.size +
                "\nYou needed at least " + REQUIRED_CORRECT + " correct answers.");
        }
        resultLabel.setVisible(true);

        // Change next button text
        nextButton.setText("Continue");
        nextButton.setVisible(true);
    }

    public void update(float delta) {
        stage.act(delta);
    }

    public void render(SpriteBatch batch) {
        stage.draw();
    }

    public boolean isQuizCompleted() {
        return quizCompleted;
    }

    public boolean isQuizPassed() {
        return quizPassed;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
