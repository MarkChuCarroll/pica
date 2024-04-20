use super::LexicalError;

#[derive(Debug)]
pub struct LineNumberIndex<'a> {
    input: &'a str,
    length: usize,
    newline_positions: Vec<usize>,
}

impl<'a> LineNumberIndex<'a> {
    pub fn new(input: &'a str) -> LineNumberIndex<'a> {
        let mut nls: Vec<usize> = Vec::new();
        let length = input.chars().count();
        for (idx, c) in input.char_indices() {
            if c == '\n' {
                nls.push(idx)
            }
        }
        LineNumberIndex {
            input: input,
            length: length,
            newline_positions: nls,
        }
    }

    pub fn number_of_lines(&self) -> usize {
        self.newline_positions.len() + 1
    }

    pub fn line_and_col_of(&self, pos: usize) -> Result<(usize, usize), LexicalError> {
        if pos > self.input.len() {
            Err(LexicalError {
                msg: format!("Position {} is beyond the end of the input", pos),
                line: 0,
                column: 0,
            })
        } else {
            let line_number = self
                .newline_positions
                .partition_point(|i| i.to_owned() < pos);
            let col = if line_number == 0 {
                pos
            } else {
                pos - self.newline_positions[line_number - 1]
            };
            Ok((line_number + 1, col))
        }
    }
}

#[cfg(test)]
mod test {
    use super::*;

    #[test]
    fn test_line_and_col_of() {
        let test_str = "line 0\nline number 1\nlineish 2\nand three\ntheeend\n";
        let index = LineNumberIndex::new(test_str);
        assert_eq!((1, 4), index.line_and_col_of(4).unwrap());
        assert_eq!((2, 11), index.line_and_col_of(17).unwrap())
    }

    #[test]
    fn test_length() {
        let test_str = "line 0\nline number 1\nlineish 2\nand three\ntheeend\n";
        let index = LineNumberIndex::new(test_str);
        assert_eq!(49, index.length);
        assert_eq!(6, index.number_of_lines())
    }
}
