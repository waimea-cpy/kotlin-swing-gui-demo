/**
 * DEMO APP
 *
 * This app shows of various features of the Swing GUI...
 * - Setting up the main window with title, size, icon, etc.
 * - Showing text labels
 * - Showing buttons, and acting upon clicks
 * - Showing images (loaded from the images folder, they are added as icons to JLabels)
 * - Playing sounds (loaded from the sounds folder)
 * - Opening dialog pop-up windows (see the ExampleDialog class at end)
 * - Responding to mouse clicks on buttons (via ActionListener)
 * - Responding to global key presses (via KeyEvents)
 * - Using different fonts
 */

import com.formdev.flatlaf.FlatLaf
import com.formdev.flatlaf.FlatLightLaf
import java.awt.*
import java.awt.event.*
import javax.sound.sampled.AudioSystem
import javax.swing.*


//=============================================================================================

/**
 * Data class for Things
 */
class ExampleClass(val name: String, val awesomeness: Int)



//=============================================================================================

/**
 * GUI class
 * Defines the UI and responds to events
 */
class GUI(private val example: MutableList<ExampleClass>) : JFrame(), ActionListener, KeyEventDispatcher {

    // Setup some properties to hold the UI elements
    private lateinit var exampleLabel: JLabel
    private lateinit var exampleImageLabel: JLabel
    private lateinit var exampleButton: JButton
    private lateinit var dialogButton: JButton

    // Timers
    private lateinit var exampleTimer: Timer

    // Dialogs
    private var exampleDialog = ExampleDialog()


    /**
     * Create, build and run the UI
     */
    init {
        setupWindow()
        buildUI()
        setupTimers()

        // Show the app, centred on screen
        setLocationRelativeTo(null)
        isVisible = true
    }

    /**
     * Configure the main window
     */
    private fun setupWindow() {
        title = "Hello, World!"
        iconImage = ImageIcon("src/images/example.png").image
        contentPane.preferredSize = Dimension(300, 440)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isResizable = false
        layout = null

        pack()
    }

    /**
     * Populate the UI
     */
    private fun buildUI() {
        val flatLafFont = FlatLaf.getPreferredFontFamily()
        val baseFont = Font(flatLafFont, Font.PLAIN, 20)
        val smallFont = Font(flatLafFont, Font.PLAIN, 14)
        val bigFont = Font(flatLafFont, Font.PLAIN, 40)

        exampleLabel = JLabel("Hello, World!", SwingConstants.CENTER)
        exampleLabel.bounds = Rectangle(30, 30, 240, 50)
        exampleLabel.font = bigFont
        add(exampleLabel)

        // A fixed label (no need to declare it in advance)
        // The <html> tag at the start of the text enables word-wrapping
        val helpMessage = "<html>Press the buttons to trigger actions, or press TAB to show the help dialog and ESC to close it."
        val helpLabel = JLabel(helpMessage)
        helpLabel.bounds = Rectangle(30, 90, 240, 80)
        helpLabel.font = smallFont
        add(helpLabel)

        var exampleImage = ImageIcon("src/images/example.png").image
        exampleImage = exampleImage.getScaledInstance(140,140, Image.SCALE_SMOOTH)
        exampleImageLabel = JLabel()
        exampleImageLabel.bounds = Rectangle(80, 190, 140, 140)
        exampleImageLabel.icon = ImageIcon(exampleImage)
        add(exampleImageLabel)

        exampleButton = JButton("Click Me")
        exampleButton.bounds = Rectangle(30,360,160,50)
        exampleButton.font = baseFont
        exampleButton.addActionListener(this)
        add(exampleButton)

        dialogButton = JButton("?")
        dialogButton.bounds = Rectangle(220,360,50,50)
        dialogButton.font = baseFont
        dialogButton.addActionListener(this)
        add(dialogButton)
    }

    /**
     * Setup recurring timers
     */
    private fun setupTimers() {
        val update = ActionListener {
            // Timer actions go here
        }
        exampleTimer = Timer(100, update)

        // Call exampleTimer.start() and exampleTimer.stop()
    }

    /**
     * Handle any UI events
     */
    override fun actionPerformed(e: ActionEvent?) {
        when (e?.source) {
            exampleButton -> exampleAction()
            dialogButton  -> showExampleDialog()
        }
    }

    /**
     * Handle any global key presses
     */
    override fun dispatchKeyEvent(e: KeyEvent?): Boolean {
        // Was it a key press event?
        if(e?.id == KeyEvent.KEY_PRESSED) {
            // Take action
            when (e.keyCode) {
                KeyEvent.VK_TAB    -> showExampleDialog()
                KeyEvent.VK_ESCAPE -> closeExampleDialog()
            }
        }
        // Allow the event to be redispatched
        return false
    }


    /**
     * An Example Action
     */
    private fun exampleAction() {
        exampleLabel.text = "DING DING!"

        val exampleSound = this::class.java.getResourceAsStream("sounds/example.wav")
        val stream = AudioSystem.getAudioInputStream(exampleSound)
        val clip = AudioSystem.getClip()
        clip.open(stream)
        clip.start()
    }


    /**
     * Show the example dialog
     */
    private fun showExampleDialog() {
        exampleDialog.setLocationRelativeTo(null)
        exampleDialog.isVisible = true
    }


    /**
     * Close the example dialog
     */
    private fun closeExampleDialog() {
        exampleDialog.isVisible = false
    }
}


//=============================================================================================

/**
 * Example Dialog class
 * Example description
 */
class ExampleDialog: JDialog() {
    private val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 20)

    init {
        setupWindow()
        buildUI()
    }

    private fun setupWindow() {
        title = "Instructions"
        contentPane.preferredSize = Dimension(200, 100)
        contentPane.background = Color.ORANGE
        isResizable = false
        isModal = true
        layout = null
        pack()
    }

    private fun buildUI() {
        val messageLabel = JLabel("Pop!", SwingConstants.CENTER)
        messageLabel.bounds = Rectangle(20,20,160,60)
        messageLabel.font = baseFont
        add(messageLabel)
    }
}


/**
 * Launch the application
 */
fun main() {
    // Flat, look-and-feel
    FlatLightLaf.setup()

    // Data stores
    val exampleData = mutableListOf<ExampleClass>()
    exampleData.add(ExampleClass("Sally", 50))
    exampleData.add(ExampleClass("Barry", 10))
    exampleData.add(ExampleClass("Frank", 30))

    // Create the UI, passing in the demo data
    val mainWindow = GUI(exampleData)

    // Allow GUI to listen for global key-presses
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(mainWindow)
}



