---
name: git-commit
description: Generates commit messages following Conventional Commits convention consistent with the java-learning repository history.
---

# Skill: Git Commit Message Generator

## When to use

When the user asks to generate a commit message or to make a commit.

## Convention (based on repository history)

### Format

```
<type>(<scope>): <short description>
```

Optionally, for complex changes, add a **body** separated by a blank line:

```
<type>(<scope>): <short description>

- bullet point 1
- bullet point 2
```

### Allowed types

| Type         | When to use                                                          | Example from repo                                                |
| ------------ | -------------------------------------------------------------------- | ---------------------------------------------------------------- |
| `feat`       | New functionality, completed lesson with code, new production code    | `feat(module-01): use ResponseEntity and DTOs in controllers`    |
| `refactor`   | Code structure change WITHOUT behavior change                        | `refactor(docs): reorganize snippets structure`                  |
| `docs`       | Changes ONLY in documentation (README, lessons, roadmaps)            | `docs(module-04): add knowledge checks and mark lessons 1-3`    |
| `chore`      | Config, IDE, .gitignore, tooling — no code impact                    | `chore: update documentation and ignore system files`            |
| `fix`        | Bug fix                                                              | (not yet used, but allowed)                                      |
| `test`       | Adding or changing tests                                             | (not yet used, but allowed)                                      |

### Scope

Scope is **optional** but **recommended**. Identifies the area of change:

| Scope            | When                                                       |
| ---------------- | ---------------------------------------------------------- |
| `module-XX`      | Changes in a specific learning module (e.g., `module-04`)  |
| `projects`       | Changes in the `projects/` directory (e.g., wallet-manager)|
| `docs`           | Changes only in the `docs/` directory                      |
| no scope         | Global, config, or cross-module changes                    |

### Description rules

1. **Language:** English (entire history is in English).
2. **Tense:** Imperative mood (`add`, `fix`, `update`, `extract` — NOT `added`, `fixes`, `updated`).
3. **Lesson commits:** For module lesson completions, use format `Lesson N — short topic description`. E.g., `feat(module-04): Lesson 4 — extract repository interfaces, hexagonal architecture`.
4. **Casing:** Lowercase after colon, except for proper nouns like `Lesson`. E.g., `feat(module-01): add instrument record`.
5. **Length:** First line max 72 characters. Keep it short and concise — avoid verbose descriptions.
6. **No period** at the end of the first line.
7. **Body:** Use bullet points only when the commit covers 3+ distinct logical changes. Keep bullets brief.

## Generation procedure

1. **Inspect changes:** Run `git diff --cached --stat` (staged) or `git diff --stat` (unstaged) to see what changed.
2. **Determine type:** Is it new code (`feat`), restructuring (`refactor`), docs only (`docs`), or config (`chore`)?
3. **Determine scope:** Which module/directory do changes belong to? If changes span multiple modules, use the dominant one or omit scope.
4. **Write description:** Short, concrete, imperative mood, English. Prefer a single concise line over verbose multi-bullet body.
5. **Optionally add body:** Only for complex commits with 3+ distinct changes.

## Examples from repository

```
feat(module-04): Lesson 4 — extract repository interfaces, hexagonal architecture
feat(module-03): complete module — add Lombok, translate lessons to EN
docs(module-04): add knowledge checks and mark lessons 1-3 as completed
refactor(docs): reorganize snippets structure
chore: update documentation and ignore system files
feat(projects): add wallet-manager Spring Boot project
```

## Notes

- If the user asked for a commit message, **provide a ready-to-copy message**.
- If the user asked to **make a commit**, show the message first and ask for approval, then run `git add -A && git commit -m "<message>"`.
- Do NOT commit automatically without user confirmation.
