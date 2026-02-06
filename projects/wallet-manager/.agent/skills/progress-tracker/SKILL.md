---
name: progress-tracker
description: Sumarize current progress and provide recommendations for next steps.
---

## Description

This skill synchronizes the learning state (documentation) with the actual code state (project). It analyzes completed lessons, updates README/Roadmap files, and verifies if the code in `projects/wallet-manager` reflects the learned concepts.

## Workflow

### 1. 🔍 Status Analysis

Analyzes the `modules/` directory to check which exercises have been physically completed.

- Checks for the existence of files in `modules/module-XX/exercises/`.
- Checks for changes in `projects/wallet-manager`.

### 2. 📝 Documentation Sync

Updates progress tracking files:

- **`README.md`**: Updates the progress table (⚪ Not Started, 🟡 In Progress, 🟢 Done).
- **`roadmap.md`**: Checks off specific items in module sections.

### 3. ⚖️ Code Gap Analysis

Compares the requirements of completed lessons with the code in `projects/wallet-manager`.

- Are appropriate structures used (e.g., Records vs Class)?
- Does the architecture match the current stage (e.g., Controller -> Service)?
- Detects "Educational Debt" (using old methods despite learning new ones).

### 4. 🚀 Recommendation Engine

Generates a final report:

- **Missing Features**: What is missing in the project relative to the acquired knowledge.
- **Next Lesson**: Suggested next lesson.
- **Theory Reading**: Suggested chapters from `docs/theory/` matching the current problem.

## Usage

Activate this skill when the user asks for "status", "what's next", or "summary".
The script should automatically correct `.md` files and print a report to the console.
